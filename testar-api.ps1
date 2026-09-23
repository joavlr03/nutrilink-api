# =============================================================================
#  NutriLink API - teste automatizado de endpoints (Windows PowerShell 5.1 ou 7+)
#
#  Uso (com a API rodando em outro terminal):
#     powershell -ExecutionPolicy Bypass -File .\testar-api.ps1
#
#  Opcional: outra porta/host
#     powershell -ExecutionPolicy Bypass -File .\testar-api.ps1 -Root http://localhost:9000
#
#  O script cria dados novos a cada execucao (CPF e registros aleatorios),
#  entao pode ser rodado varias vezes sem limpar o banco.
# =============================================================================

param(
    [string]$Root = "http://localhost:9000"
)

$ErrorActionPreference = "Continue"
$ProgressPreference = "SilentlyContinue"   # deixa o Invoke-WebRequest bem mais rapido no PS 5.1

$V = "/api/v2"
$results = New-Object System.Collections.Generic.List[object]
$ZERO_UUID = "00000000-0000-0000-0000-000000000000"

# -----------------------------------------------------------------------------
#  Helpers
# -----------------------------------------------------------------------------

function Invoke-Api {
    param(
        [string]$Method,
        [string]$Path,
        $Body = $null,
        [switch]$RawBody
    )

    $params = @{
        Uri             = "$Root$Path"
        Method          = $Method
        UseBasicParsing = $true
        TimeoutSec      = 30
        Headers         = @{ Accept = "application/json, */*" }
    }

    if ($null -ne $Body) {
        if ($RawBody) { $json = [string]$Body }
        else          { $json = $Body | ConvertTo-Json -Depth 10 -Compress }
        $params.Body        = [System.Text.Encoding]::UTF8.GetBytes($json)
        $params.ContentType = "application/json; charset=utf-8"
    }

    $status = 0
    $content = ""
    try {
        $resp = Invoke-WebRequest @params
        $status = [int]$resp.StatusCode
        $content = [string]$resp.Content
    }
    catch {
        if ($_.Exception.Response) {
            $errResp = $_.Exception.Response
            $status = [int]$errResp.StatusCode
            $content = [string]$_.ErrorDetails.Message

            # Se o PowerShell nao preencheu ErrorDetails, le o corpo direto da resposta
            if (-not $content) {
                try {
                    if ($errResp -is [System.Net.HttpWebResponse]) {
                        # Windows PowerShell 5.1
                        $stream = $errResp.GetResponseStream()
                        if ($stream.CanSeek) { $stream.Position = 0 }
                        $reader = New-Object System.IO.StreamReader($stream, [System.Text.Encoding]::UTF8)
                        $content = $reader.ReadToEnd()
                        $reader.Close()
                    }
                    elseif ($errResp.Content) {
                        # PowerShell 7+
                        $content = $errResp.Content.ReadAsStringAsync().GetAwaiter().GetResult()
                    }
                }
                catch { }
            }
            if (-not $content) { $content = "(corpo vazio)" }
        }
        else {
            $status = 0
            $content = $_.Exception.Message
        }
    }

    $data = $null
    if ($content) {
        try { $data = $content | ConvertFrom-Json } catch { }
    }

    return [pscustomobject]@{ Status = $status; Data = $data; Content = $content }
}

function Test-Step {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Path,
        $Body = $null,
        [int]$Expected,
        [hashtable]$Fields = @{},      # campos da resposta que devem ter um valor exato
        [string[]]$HasFields = @(),    # campos que devem existir na resposta
        [int]$MinCount = -1,           # para listagens: quantidade minima de itens
        [string]$ContentContains = "", # texto que deve aparecer no corpo da resposta
        [string]$Hint = "",            # dica exibida se o teste falhar
        [switch]$RawBody
    )

    $r = Invoke-Api -Method $Method -Path $Path -Body $Body -RawBody:$RawBody
    $problems = @()

    if ($r.Status -ne $Expected) {
        $problems += "HTTP esperado $Expected, recebido $($r.Status)"
    }
    else {
        foreach ($k in $Fields.Keys) {
            $actual = $null
            if ($r.Data) { $actual = $r.Data.$k }
            if ("$actual" -ne "$($Fields[$k])") {
                $problems += "campo '$k' esperado '$($Fields[$k])', recebido '$actual'"
            }
        }
        foreach ($k in $HasFields) {
            if (-not $r.Data -or -not ($r.Data.PSObject.Properties.Name -contains $k)) {
                $problems += "campo '$k' ausente na resposta"
            }
        }
        if ($MinCount -ge 0 -and @($r.Data).Count -lt $MinCount) {
            $problems += "esperado ao menos $MinCount itens, recebido $(@($r.Data).Count)"
        }
        if ($ContentContains -and ($r.Content -notlike "*$ContentContains*")) {
            $problems += "resposta nao contem '$ContentContains'"
        }
    }

    $ok = ($problems.Count -eq 0)
    $label = "{0,-6} {1}" -f $Method, $Path

    if ($ok) {
        Write-Host ("  [OK]    {0}  ->  {1}" -f $Name, $r.Status) -ForegroundColor Green
    }
    else {
        Write-Host ("  [FALHA] {0}" -f $Name) -ForegroundColor Red
        Write-Host ("          {0}" -f $label) -ForegroundColor DarkGray
        foreach ($p in $problems) { Write-Host "          - $p" -ForegroundColor Red }
        if ($r.Content) {
            $snippet = $r.Content
            if ($snippet.Length -gt 400) { $snippet = $snippet.Substring(0, 400) + "..." }
            Write-Host "          resposta: $snippet" -ForegroundColor DarkGray
        }
        if ($Hint) { Write-Host "          dica: $Hint" -ForegroundColor Yellow }
    }

    $results.Add([pscustomobject]@{
        Ok       = $ok
        Name     = $Name
        Request  = $label
        Status   = $r.Status
        Expected = $Expected
        Problems = ($problems -join "; ")
        Response = $r.Content
    })

    return $r
}

function Section([string]$title) {
    Write-Host ""
    Write-Host "== $title ==" -ForegroundColor Cyan
}

function Get-Id($response, [string]$what) {
    if ($response -and $response.Data -and $response.Data.id) { return [string]$response.Data.id }
    Write-Host "          ATENCAO: nao foi possivel obter o ID de $what. Testes que dependem dele vao falhar." -ForegroundColor Yellow
    return $ZERO_UUID
}

# -----------------------------------------------------------------------------
#  Dados de teste (aleatorios para permitir rodar varias vezes)
# -----------------------------------------------------------------------------

function New-Cpf { -join (1..11 | ForEach-Object { Get-Random -Minimum 0 -Maximum 10 }) }

$sfx         = Get-Random -Minimum 100000 -Maximum 999999
$dataFutura  = (Get-Date).AddDays(30).ToString("yyyy-MM-dd'T09:00:00'")
$dataPassada = (Get-Date).AddDays(-1).ToString("yyyy-MM-dd'T09:00:00'")
$nascMenor   = (Get-Date).AddYears(-15).ToString("yyyy-MM-dd")

function New-Doadora([string]$cpf, [string]$nascimento = "1995-04-20") {
    return @{
        nomeCompleto     = "Maria Teste $sfx"
        cpf              = $cpf
        dataNascimento   = $nascimento
        telefone         = "11987654321"
        cep              = "07010000"
        enderecoCompleto = "Rua das Flores, 100 - Centro"
    }
}

$questionarioOk      = '{"medicamento":false,"doencaCronica":false,"alcool":false,"fumo":false}'
$questionarioRevisao = '{"medicamento":true,"doencaCronica":false,"alcool":false,"fumo":false}'

# -----------------------------------------------------------------------------
#  0. API no ar?
# -----------------------------------------------------------------------------

Write-Host ""
Write-Host "NutriLink API - testes automatizados em $Root" -ForegroundColor White

$ping = Invoke-Api -Method GET -Path "$V/doadoras"
if ($ping.Status -eq 0) {
    Write-Host ""
    Write-Host "A API nao respondeu em $Root." -ForegroundColor Red
    Write-Host "Suba a aplicacao em outro terminal com:  mvnw.cmd spring-boot:run" -ForegroundColor Yellow
    Write-Host "Detalhe: $($ping.Content)" -ForegroundColor DarkGray
    exit 1
}

Section "Documentacao"
Test-Step -Name "Swagger UI na raiz" -Method GET -Path "/" -Expected 200 | Out-Null
Test-Step -Name "OpenAPI lista o endpoint de sincronizacoes" -Method GET -Path "/v3/api-docs" -Expected 200 `
    -ContentContains "/api/v2/sincronizacoes" `
    -Hint "O SincronizacaoController foi adicionado ao projeto?" | Out-Null

# -----------------------------------------------------------------------------
#  1. Profissionais de saude
# -----------------------------------------------------------------------------

Section "Profissionais de saude"

$r = Test-Step -Name "Cadastrar especialista em lactacao" -Method POST -Path "$V/profissionais-saude" -Expected 201 `
    -Body @{ nomeCompleto = "Dra. Ana Souza"; registroConselho = "CRM-SP $sfx"; tipoProfissional = "ESPECIALISTA_LACTACAO" } `
    -Fields @{ credencialAtiva = "True"; tipoProfissional = "ESPECIALISTA_LACTACAO" }
$ESP = Get-Id $r "especialista"

$r = Test-Step -Name "Cadastrar analista nivel 1" -Method POST -Path "$V/profissionais-saude" -Expected 201 `
    -Body @{ nomeCompleto = "Carlos Lima"; registroConselho = "COREN-SP $sfx"; tipoProfissional = "ANALISTA_NIVEL_1" }
$ANA = Get-Id $r "analista"

Test-Step -Name "Registro de conselho duplicado" -Method POST -Path "$V/profissionais-saude" -Expected 400 `
    -Body @{ nomeCompleto = "Outro"; registroConselho = "CRM-SP $sfx"; tipoProfissional = "ANALISTA_NIVEL_1" } | Out-Null
Test-Step -Name "Tipo de profissional inexistente (enum invalido)" -Method POST -Path "$V/profissionais-saude" -Expected 400 `
    -Body @{ nomeCompleto = "Outro"; registroConselho = "X-$sfx"; tipoProfissional = "MEDICO" } | Out-Null
Test-Step -Name "Buscar profissional por ID" -Method GET -Path "$V/profissionais-saude/$ESP" -Expected 200 | Out-Null
Test-Step -Name "Listar profissionais" -Method GET -Path "$V/profissionais-saude" -Expected 200 -MinCount 2 | Out-Null
Test-Step -Name "Listar profissionais ativos" -Method GET -Path "$V/profissionais-saude/ativos" -Expected 200 -MinCount 2 | Out-Null
Test-Step -Name "Listar por tipo" -Method GET -Path "$V/profissionais-saude/tipo/ESPECIALISTA_LACTACAO" -Expected 200 -MinCount 1 | Out-Null
Test-Step -Name "Inativar credencial" -Method PATCH -Path "$V/profissionais-saude/$ANA/inativar" -Expected 200 `
    -Fields @{ credencialAtiva = "False" } | Out-Null
Test-Step -Name "Reativar credencial" -Method PATCH -Path "$V/profissionais-saude/$ANA/reativar" -Expected 200 `
    -Fields @{ credencialAtiva = "True" } | Out-Null

# -----------------------------------------------------------------------------
#  2. Doadoras
# -----------------------------------------------------------------------------

Section "Doadoras"

$cpf1 = New-Cpf
$r = Test-Step -Name "Cadastrar doadora (status inicial PENDENTE)" -Method POST -Path "$V/doadoras" -Expected 201 `
    -Body (New-Doadora $cpf1) -Fields @{ statusCadastro = "PENDENTE" }
$DOA = Get-Id $r "doadora principal"

$r = Test-Step -Name "Cadastrar segunda doadora (para revisao humana)" -Method POST -Path "$V/doadoras" -Expected 201 `
    -Body (New-Doadora (New-Cpf))
$DOA2 = Get-Id $r "segunda doadora"

Test-Step -Name "CPF duplicado" -Method POST -Path "$V/doadoras" -Expected 400 -Body (New-Doadora $cpf1) | Out-Null
Test-Step -Name "Doadora menor de idade" -Method POST -Path "$V/doadoras" -Expected 400 `
    -Body (New-Doadora (New-Cpf) $nascMenor) | Out-Null
Test-Step -Name "CPF com tamanho invalido (erro lista os campos)" -Method POST -Path "$V/doadoras" -Expected 400 `
    -Body (New-Doadora "123") -HasFields @("campos") `
    -Hint "Sem 'campos' na resposta: o GlobalExceptionHandler esta no projeto?" | Out-Null
Test-Step -Name "Campos obrigatorios ausentes" -Method POST -Path "$V/doadoras" -Expected 400 -Body @{ nomeCompleto = "Ana" } | Out-Null
Test-Step -Name "JSON malformado" -Method POST -Path "$V/doadoras" -Expected 400 -Body '{"nomeCompleto": ' -RawBody | Out-Null
Test-Step -Name "Buscar doadora por ID" -Method GET -Path "$V/doadoras/$DOA" -Expected 200 | Out-Null
Test-Step -Name "Listar doadoras" -Method GET -Path "$V/doadoras" -Expected 200 -MinCount 2 | Out-Null
Test-Step -Name "ID inexistente" -Method GET -Path "$V/doadoras/$ZERO_UUID" -Expected 404 `
    -Hint "Se veio 500, falta o GlobalExceptionHandler." | Out-Null
Test-Step -Name "ID em formato invalido" -Method GET -Path "$V/doadoras/abc" -Expected 400 | Out-Null

# -----------------------------------------------------------------------------
#  3. Corredores logisticos
# -----------------------------------------------------------------------------

Section "Corredores logisticos"

$r = Test-Step -Name "Cadastrar corredor (ja homologado)" -Method POST -Path "$V/logistica" -Expected 201 `
    -Body @{ nomeCorredor = "Corredor Guarulhos $sfx"; cepsAtendidos = "070,071,072" } `
    -Fields @{ statusHomologacao = "True" }
$COR = Get-Id $r "corredor principal"

$r = Test-Step -Name "Cadastrar corredor de outra regiao" -Method POST -Path "$V/logistica" -Expected 201 `
    -Body @{ nomeCorredor = "Corredor Distante $sfx"; cepsAtendidos = "999" }
$COR2 = Get-Id $r "corredor distante"

Test-Step -Name "Corredor sem campos obrigatorios" -Method POST -Path "$V/logistica" -Expected 400 -Body @{ nomeCorredor = "" } | Out-Null
Test-Step -Name "Buscar corredor por ID" -Method GET -Path "$V/logistica/$COR" -Expected 200 | Out-Null
Test-Step -Name "Listar corredores" -Method GET -Path "$V/logistica" -Expected 200 -MinCount 2 | Out-Null
Test-Step -Name "Listar corredores ativos" -Method GET -Path "$V/logistica/ativos" -Expected 200 -MinCount 1 | Out-Null

# -----------------------------------------------------------------------------
#  4. Triagem
# -----------------------------------------------------------------------------

Section "Triagem"

Test-Step -Name "Coleta para doadora ainda PENDENTE e bloqueada" -Method POST -Path "$V/coleta" -Expected 409 `
    -Body @{ doadoraId = $DOA; corredorId = $COR; dataAgendada = $dataFutura; volumeEstimadoMl = 300 } `
    -Hint "Se veio 400, confira o import do @RequestBody no ColetaController. Se veio 500, falta o GlobalExceptionHandler." | Out-Null

$r = Test-Step -Name "Triagem aprovada (score 100)" -Method POST -Path "$V/triagens" -Expected 201 `
    -Body @{ doadoraId = $DOA; respostasQuestionario = $questionarioOk } `
    -Fields @{ scoreRisco = "100"; statusTriagem = "APROVADA"; requerValidacaoHumana = "False" }
$TRI = Get-Id $r "triagem"

Test-Step -Name "Doadora passou para APROVADA" -Method GET -Path "$V/doadoras/$DOA" -Expected 200 `
    -Fields @{ statusCadastro = "APROVADA" } | Out-Null

Test-Step -Name "Triagem que exige revisao humana (score 60)" -Method POST -Path "$V/triagens" -Expected 201 `
    -Body @{ doadoraId = $DOA2; respostasQuestionario = $questionarioRevisao } `
    -Fields @{ scoreRisco = "60"; statusTriagem = "PENDENTE_REVISAO"; requerValidacaoHumana = "True" } | Out-Null

Test-Step -Name "Triagem com especialista no lugar de analista" -Method POST -Path "$V/triagens" -Expected 400 `
    -Body @{ doadoraId = $DOA2; profissionalId = $ESP; respostasQuestionario = $questionarioOk } | Out-Null
Test-Step -Name "Triagem sem questionario" -Method POST -Path "$V/triagens" -Expected 400 -Body @{ doadoraId = $DOA2 } | Out-Null
Test-Step -Name "Buscar triagem por ID" -Method GET -Path "$V/triagens/$TRI" -Expected 200 | Out-Null
Test-Step -Name "Listar triagens" -Method GET -Path "$V/triagens" -Expected 200 -MinCount 2 | Out-Null
Test-Step -Name "Listar triagens da doadora" -Method GET -Path "$V/triagens/doadora/$DOA" -Expected 200 -MinCount 1 | Out-Null
Test-Step -Name "Listar triagens pendentes de revisao" -Method GET -Path "$V/triagens/pendentes-revisao" -Expected 200 -MinCount 1 | Out-Null

# -----------------------------------------------------------------------------
#  5. Coletas
# -----------------------------------------------------------------------------

Section "Coletas"

$r = Test-Step -Name "Agendar coleta" -Method POST -Path "$V/coleta" -Expected 201 `
    -Body @{ doadoraId = $DOA; corredorId = $COR; dataAgendada = $dataFutura; volumeEstimadoMl = 300 } `
    -Fields @{ statusColeta = "AGENDADA" } `
    -Hint "Se veio 400 com todos os campos nulos, o ColetaController ainda importa o RequestBody do Swagger."
$COL = Get-Id $r "coleta"

Test-Step -Name "Coleta com data no passado" -Method POST -Path "$V/coleta" -Expected 400 `
    -Body @{ doadoraId = $DOA; corredorId = $COR; dataAgendada = $dataPassada } | Out-Null
Test-Step -Name "CEP da doadora fora do corredor" -Method POST -Path "$V/coleta" -Expected 400 `
    -Body @{ doadoraId = $DOA; corredorId = $COR2; dataAgendada = $dataFutura } | Out-Null
Test-Step -Name "Desabilitar corredor" -Method PATCH -Path "$V/logistica/$COR2/desabilitar" -Expected 200 `
    -Fields @{ statusHomologacao = "False" } | Out-Null
Test-Step -Name "Coleta em corredor desabilitado" -Method POST -Path "$V/coleta" -Expected 409 `
    -Body @{ doadoraId = $DOA; corredorId = $COR2; dataAgendada = $dataFutura } | Out-Null
Test-Step -Name "Habilitar corredor" -Method PATCH -Path "$V/logistica/$COR2/habilitar" -Expected 200 `
    -Fields @{ statusHomologacao = "True" } | Out-Null
Test-Step -Name "Atualizar status para EM_ROTA" -Method PATCH -Path "$V/coleta/$COL/status/EM_ROTA" -Expected 200 `
    -Fields @{ statusColeta = "EM_ROTA" } | Out-Null
Test-Step -Name "Status de coleta inexistente na URL" -Method PATCH -Path "$V/coleta/$COL/status/VOANDO" -Expected 400 | Out-Null
Test-Step -Name "Buscar coleta por ID" -Method GET -Path "$V/coleta/$COL" -Expected 200 | Out-Null
Test-Step -Name "Listar coletas" -Method GET -Path "$V/coleta" -Expected 200 -MinCount 1 | Out-Null
Test-Step -Name "Listar coletas da doadora" -Method GET -Path "$V/coleta/doadora/$DOA" -Expected 200 -MinCount 1 | Out-Null
Test-Step -Name "Listar coletas por status" -Method GET -Path "$V/coleta/status/EM_ROTA" -Expected 200 -MinCount 1 | Out-Null

# -----------------------------------------------------------------------------
#  6. Sincronizacao
# -----------------------------------------------------------------------------

Section "Sincronizacao"

$r = Test-Step -Name "Registrar sincronizacao da coleta" -Method POST -Path "$V/sincronizacoes" -Expected 201 `
    -Body @{ coletaId = $COL } -Fields @{ statusSincronizacao = "PENDENTE" } -HasFields @("payloadEnviado")
$SYN = Get-Id $r "sincronizacao"

Test-Step -Name "Segunda sincronizacao para a mesma coleta" -Method POST -Path "$V/sincronizacoes" -Expected 409 -Body @{ coletaId = $COL } | Out-Null
Test-Step -Name "Registrar falha no envio" -Method PATCH -Path "$V/sincronizacoes/$SYN/falha" -Expected 200 `
    -Fields @{ statusSincronizacao = "FALHA" } | Out-Null
Test-Step -Name "Confirmar sincronizacao com FALHA e bloqueado" -Method PATCH -Path "$V/sincronizacoes/$SYN/confirmar" -Expected 409 `
    -Body @{ protocoloGerado = "BLH-$sfx" } | Out-Null
Test-Step -Name "Reprocessar (volta para PENDENTE)" -Method PATCH -Path "$V/sincronizacoes/$SYN/reprocessar" -Expected 200 `
    -Fields @{ statusSincronizacao = "PENDENTE" } | Out-Null
Test-Step -Name "Confirmar sem protocolo" -Method PATCH -Path "$V/sincronizacoes/$SYN/confirmar" -Expected 400 `
    -Body @{ protocoloGerado = "" } | Out-Null
Test-Step -Name "Confirmar com protocolo" -Method PATCH -Path "$V/sincronizacoes/$SYN/confirmar" -Expected 200 `
    -Body @{ protocoloGerado = "BLH-$sfx" } -Fields @{ statusSincronizacao = "SUCESSO"; protocoloGerado = "BLH-$sfx" } | Out-Null
Test-Step -Name "Buscar sincronizacao por ID" -Method GET -Path "$V/sincronizacoes/$SYN" -Expected 200 | Out-Null
Test-Step -Name "Buscar sincronizacao da coleta" -Method GET -Path "$V/sincronizacoes/coleta/$COL" -Expected 200 | Out-Null
Test-Step -Name "Listar sincronizacoes" -Method GET -Path "$V/sincronizacoes" -Expected 200 -MinCount 1 | Out-Null
Test-Step -Name "Listar sincronizacoes por status" -Method GET -Path "$V/sincronizacoes/status/SUCESSO" -Expected 200 -MinCount 1 | Out-Null

# -----------------------------------------------------------------------------
#  7. Suporte: tickets e mensagens
# -----------------------------------------------------------------------------

Section "Tickets e mensagens de suporte"

$r = Test-Step -Name "Abrir ticket" -Method POST -Path "$V/tickets-suporte" -Expected 201 `
    -Body @{ doadoraId = $DOA; assunto = "Duvida sobre armazenamento do leite" } -Fields @{ statusTicket = "ABERTO" }
$TIC = Get-Id $r "ticket"

Test-Step -Name "Ticket sem assunto" -Method POST -Path "$V/tickets-suporte" -Expected 400 -Body @{ doadoraId = $DOA } | Out-Null
Test-Step -Name "Analista tentando assumir ticket" -Method PATCH -Path "$V/tickets-suporte/$TIC/assumir/$ANA" -Expected 400 | Out-Null
Test-Step -Name "Especialista assume ticket" -Method PATCH -Path "$V/tickets-suporte/$TIC/assumir/$ESP" -Expected 200 `
    -Fields @{ statusTicket = "EM_ANDAMENTO"; profissionalId = $ESP } | Out-Null

Test-Step -Name "Mensagem da doadora" -Method POST -Path "$V/mensagens-suporte" -Expected 201 `
    -Body @{ ticketId = $TIC; remetenteTipo = "DOADORA"; remetenteId = $DOA; conteudoMensagem = "Posso congelar o leite em pote de vidro?" } | Out-Null
$r = Test-Step -Name "Mensagem do especialista" -Method POST -Path "$V/mensagens-suporte" -Expected 201 `
    -Body @{ ticketId = $TIC; remetenteTipo = "PROFISSIONAL"; remetenteId = $ESP; conteudoMensagem = "Pode sim, com tampa plastica e bem higienizado." }
$MSG = Get-Id $r "mensagem"

Test-Step -Name "Mensagem com remetente inexistente" -Method POST -Path "$V/mensagens-suporte" -Expected 404 `
    -Body @{ ticketId = $TIC; remetenteTipo = "DOADORA"; remetenteId = $ZERO_UUID; conteudoMensagem = "Oi" } | Out-Null
Test-Step -Name "Buscar mensagem por ID" -Method GET -Path "$V/mensagens-suporte/$MSG" -Expected 200 | Out-Null
Test-Step -Name "Listar mensagens" -Method GET -Path "$V/mensagens-suporte" -Expected 200 -MinCount 2 | Out-Null
Test-Step -Name "Listar mensagens do ticket" -Method GET -Path "$V/mensagens-suporte/ticket/$TIC" -Expected 200 -MinCount 2 | Out-Null
Test-Step -Name "Buscar ticket por ID" -Method GET -Path "$V/tickets-suporte/$TIC" -Expected 200 | Out-Null
Test-Step -Name "Listar tickets" -Method GET -Path "$V/tickets-suporte" -Expected 200 -MinCount 1 | Out-Null
Test-Step -Name "Listar tickets da doadora" -Method GET -Path "$V/tickets-suporte/doadora/$DOA" -Expected 200 -MinCount 1 | Out-Null
Test-Step -Name "Listar tickets por status" -Method GET -Path "$V/tickets-suporte/status/EM_ANDAMENTO" -Expected 200 -MinCount 1 | Out-Null
Test-Step -Name "Fechar ticket" -Method PATCH -Path "$V/tickets-suporte/$TIC/fechar" -Expected 200 `
    -Fields @{ statusTicket = "FECHADO" } | Out-Null
Test-Step -Name "Mensagem em ticket fechado e bloqueada" -Method POST -Path "$V/mensagens-suporte" -Expected 409 `
    -Body @{ ticketId = $TIC; remetenteTipo = "DOADORA"; remetenteId = $DOA; conteudoMensagem = "Mais uma duvida" } | Out-Null

# -----------------------------------------------------------------------------
#  8. Exclusoes e integridade
# -----------------------------------------------------------------------------

Section "Exclusoes e integridade"

Test-Step -Name "Excluir doadora com triagem/coleta vinculadas" -Method DELETE -Path "$V/doadoras/$DOA" -Expected 409 `
    -Hint "Se veio 500, falta o tratamento de DataIntegrityViolationException no GlobalExceptionHandler." | Out-Null

$r = Test-Step -Name "Cadastrar doadora avulsa para exclusao" -Method POST -Path "$V/doadoras" -Expected 201 -Body (New-Doadora (New-Cpf))
$DOA3 = Get-Id $r "doadora avulsa"
Test-Step -Name "Excluir doadora sem vinculos" -Method DELETE -Path "$V/doadoras/$DOA3" -Expected 204 | Out-Null
Test-Step -Name "Doadora excluida nao e mais encontrada" -Method GET -Path "$V/doadoras/$DOA3" -Expected 404 | Out-Null
Test-Step -Name "Excluir mensagem" -Method DELETE -Path "$V/mensagens-suporte/$MSG" -Expected 204 | Out-Null
Test-Step -Name "Excluir registro inexistente" -Method DELETE -Path "$V/logistica/$ZERO_UUID" -Expected 404 | Out-Null

# -----------------------------------------------------------------------------
#  Resumo
# -----------------------------------------------------------------------------

$total  = $results.Count
$passed = @($results | Where-Object { $_.Ok }).Count
$failed = $total - $passed

Write-Host ""
Write-Host "==============================================" -ForegroundColor White
if ($failed -eq 0) {
    Write-Host " RESULTADO: $passed de $total testes passaram. Tudo certo!" -ForegroundColor Green
}
else {
    Write-Host " RESULTADO: $passed de $total passaram, $failed falharam." -ForegroundColor Red
    Write-Host ""
    Write-Host " Falhas:" -ForegroundColor Red
    $results | Where-Object { -not $_.Ok } | ForEach-Object {
        Write-Host "  - $($_.Name)  [$($_.Request)]  $($_.Problems)" -ForegroundColor Red
    }
}
Write-Host "==============================================" -ForegroundColor White

# Relatorio em arquivo, para compartilhar
$reportPath = Join-Path (Get-Location) "resultado-testes.txt"
$lines = @("NutriLink API - resultado dos testes ($(Get-Date -Format 'yyyy-MM-dd HH:mm:ss'))",
           "Base: $Root", "Passaram: $passed / $total", "")
foreach ($res in $results) {
    $tag = "OK   "
    if (-not $res.Ok) { $tag = "FALHA" }
    $lines += "[$tag] $($res.Name) | $($res.Request) | esperado $($res.Expected), recebido $($res.Status)"
    if (-not $res.Ok) {
        $lines += "        $($res.Problems)"
        $lines += "        resposta: $($res.Response)"
    }
}
$lines | Out-File -FilePath $reportPath -Encoding utf8
Write-Host "Relatorio salvo em: $reportPath" -ForegroundColor DarkGray

if ($failed -gt 0) { exit 1 } else { exit 0 }
