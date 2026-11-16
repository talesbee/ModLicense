# ModLicense — Plano de implementação e especificação para agente de desenvolvimento

> **Documento de execução para agente de código.** O agente deve implementar o mod, integrações com Forge/NeoForge, testes, persistência, documentação técnica e empacotamento. O criador do projeto fornecerá os assets visuais definidos neste documento. Não inventar nem redesenhar assets sem autorização.
>
> **Status:** especificação inicial — decisões marcadas como “proposta” devem ser confirmadas antes de se tornarem contratos públicos.
>
> **Nome provisório:** ModLicense  
> **Mod ID sugerido:** `modlicense`  
> **Idioma da documentação:** português (Brasil) e inglês  
> **Objetivo:** mod de licenciamento de tecnologias para servidores Minecraft RP.

---

## 1. Visão geral do produto

ModLicense permite que administradores atribuam a jogadores a propriedade de um ou mais mods instalados no modpack. O proprietário pode emitir licenças físicas para que outros jogadores recebam permissões relacionadas aos itens e blocos daquele mod.

A licença é representada por um **pergaminho consumível**, comercializável como um item normal do Minecraft. O jogador compra ou recebe o pergaminho, usa-o com o botão direito e, após validação do servidor, recebe as permissões configuradas.

A emissão é feita por meio de um **Livro de Autorização** utilizado em um **atril vanilla**. A interação abre uma interface personalizada na qual o proprietário escolhe o mod, as permissões, a validade e as opções do documento.

### 1.1 Princípios de design

1. **Experiência vanilla:** reutilizar o atril, a aparência de livros, sons e partículas do Minecraft sempre que possível.
2. **Licença como item físico:** o pergaminho deve funcionar como um item normal de inventário, para poder ser guardado, trocado e vendido por máquinas de outros mods.
3. **Servidor como autoridade:** todas as permissões, emissões, resgates e bloqueios são validados no servidor. O cliente nunca decide se uma ação está autorizada.
4. **Independência de modpacks:** nenhum mod externo deve ser obrigatório para o funcionamento básico.
5. **Compatibilidade explícita:** cada combinação de Minecraft + loader terá seu próprio artefato e testes. Não prometer suporte universal entre versões.
6. **Integração incremental:** bloqueios genéricos primeiro; integração específica com máquinas de outros mods depois.
7. **Assets fornecidos pelo criador:** o agente implementa a utilização dos arquivos visuais, não cria nem altera a direção artística.

### 1.2 O que o produto não deve prometer

- Não afirmar que é possível identificar genericamente o jogador responsável por toda produção feita por máquinas automatizadas.
- Não afirmar que um bloqueio genérico consegue interceptar toda interação interna de qualquer mod.
- Não afirmar que a licença impede todas as formas de obter um item (baús, drops, comandos, recompensas, trocas, automação etc.).
- Não chamar uma permissão de “craft de máquinas” se ela só cobre crafting executado por um jogador na interface padrão.

Essas limitações devem aparecer na documentação e na matriz de compatibilidade.

---

## 2. Escopo funcional

### 2.1 Funcionalidades do MVP

| ID | Funcionalidade | Prioridade |
|---|---|---|
| F-001 | Atribuir proprietário a um Mod ID por comando/configuração administrativa | P0 |
| F-002 | Identificar automaticamente o namespace de itens e blocos registrados | P0 |
| F-003 | Livro de Autorização utilizável ao clicar em um atril vanilla | P0 |
| F-004 | Interface para selecionar um mod que o jogador possui | P0 |
| F-005 | Seleção de uma ou várias permissões | P0 |
| F-006 | Emissão de um pergaminho configurado | P0 |
| F-007 | Um único item registrado de pergaminho, com aparência variável conforme as permissões | P0 |
| F-008 | Pergaminho transferível e compatível com inventários e máquinas de venda | P0 |
| F-009 | Consumir pergaminho com botão direito e conceder licença | P0 |
| F-010 | Registrar licença por UUID do jogador e Mod ID | P0 |
| F-011 | Persistir proprietários, tickets emitidos e licenças no mundo/servidor | P0 |
| F-012 | Bloquear crafting manual protegido, quando a API do loader permitir interceptação segura | P0 |
| F-013 | Bloquear colocação de blocos protegidos | P0 |
| F-014 | Bloquear usos/interações cobertos pela matriz genérica | P0 |
| F-015 | Mensagens de bloqueio e tooltip informativo | P0 |
| F-016 | Comandos de consulta, revogação e administração | P0 |
| F-017 | Efeito visual/sonoro de ativação semelhante à evolução de aldeão | P1 |
| F-018 | Traduções `pt_br` e `en_us` | P0 |
| F-019 | Testes automatizados e teste em servidor dedicado | P0 |
| F-020 | Arquivo JSON administrativo com exceções por ID de item/bloco, ignorando licenças | P0 |

### 2.2 Funcionalidades posteriores

| ID | Funcionalidade | Prioridade |
|---|---|---|
| F-021 | Integração de exibição com JEI/EMI/REI | P2 |
| F-022 | API pública para outros mods consultarem permissões | P1/P2 |
| F-023 | Adaptadores específicos para máquinas (Create, Mekanism, AE2 etc.) | P2 |
| F-024 | Licenças temporárias | P1 |
| F-025 | Tickets vinculados a um UUID específico | P2 |
| F-026 | Delegados/representantes autorizados a emitir licenças | P2 |
| F-027 | Permissões de posse, drop e pickup independentes | P2 |
| F-028 | Interface para o proprietário revogar licenças | P1 |
| F-029 | Regras adicionais/individuais por item ou bloco, além da lista de exceções | P2 |
| F-030 | Integração de publicação automatizada no CurseForge/Modrinth | P2 |

Não implementar funcionalidades P2 antes de o MVP estar estável, salvo decisão expressa do responsável pelo projeto.

---

## 3. Plataforma, versões e estrutura do projeto

### 3.1 Estratégia inicial de versões

**Proposta inicial:** começar com Minecraft **1.21.1**, gerando artefatos separados para:

- Minecraft 1.21.1 + Forge;
- Minecraft 1.21.1 + NeoForge.

A escolha de 1.21.1 é uma linha de partida para reduzir a quantidade de variáveis e aproveitar um ecossistema amplo de modpacks. Antes de iniciar, o agente deve verificar as versões estáveis e os MDKs oficiais disponíveis na data da implementação e registrar no `README`/`gradle.properties` as versões exatas utilizadas.

Não implementar “todas as versões” no MVP. Após o MVP, avaliar alvos adicionais individualmente, por exemplo Forge 1.20.1 e versões posteriores de NeoForge.

**Importante:** Forge e NeoForge geram JARs diferentes. Não entregar um único JAR anunciando compatibilidade com ambos.

### 3.2 Java e toolchain

Para o alvo inicial 1.21.1, usar JDK 21, confirmando os requisitos exatos no MDK e na documentação oficial de cada loader. Não misturar classes de cliente em inicialização comum/servidor dedicado.

### 3.3 Arquitetura recomendada

Manter um núcleo de domínio compartilhado e adaptadores específicos por loader:

```text
ModLicense/
├── README.md
├── LICENSE                  # adicionar depois da decisão do proprietário
├── CONTRIBUTING.md
├── CHANGELOG.md
├── docs/
│   ├── ARCHITECTURE.md
│   ├── PERMISSIONS.md
│   ├── COMPATIBILITY.md
│   ├── ASSET-MANIFEST.md
│   └── TEST-PLAN.md
├── common/                  # regras de domínio sem dependência de classes de cliente
│   └── src/main/java/...
├── forge/                   # bootstrap, eventos, menus e integração Forge
│   └── src/main/...
├── neoforge/                # bootstrap, eventos, menus e integração NeoForge
│   └── src/main/...
├── gradle/
├── build.gradle
├── settings.gradle
└── gradle.properties
```

O agente pode propor uma estrutura Gradle equivalente se houver uma razão técnica. O requisito é compartilhar a lógica de domínio e manter separadas as integrações de loader. Evitar adicionar Architectury ou outra camada de compatibilidade sem justificar o custo/benefício.

### 3.4 Responsabilidades do núcleo compartilhado

O módulo `common` deve conter, preferencialmente em Java puro:

- enumeração `LicensePermission`;
- modelos imutáveis de licença e ticket;
- regras de combinação e validação de permissões;
- resolução de propriedade por Mod ID;
- verificação de validade/expiração;
- resultados de autorização (`ALLOW`, `DENY`, `NOT_APPLICABLE`);
- interfaces para persistência e relógio/testes;
- regras de resgate único de ticket.

Evitar colocar `Screen`, `Menu`, `Minecraft`, `Level`, `ItemStack` ou classes específicas de Forge/NeoForge no núcleo puro. Adaptadores fazem a conversão de tipos do Minecraft para os modelos do domínio.

### 3.5 Regras de implementação

- Código Java com nomes e comentários consistentes em inglês; documentação de projeto também em inglês quando for material de publicação. Especificações internas podem permanecer em português.
- Não usar reflection ou mixins como primeira opção. Preferir APIs públicas e eventos documentados.
- Não adicionar dependência obrigatória de JEI, EMI, Create ou qualquer mod de conteúdo.
- Não duplicar regras de autorização entre Forge e NeoForge. Os adaptadores devem chamar o mesmo serviço de domínio.
- Criar builds e testes separados por loader.

---

## 4. Modelo de propriedade

### 4.1 Identificação do mod

A propriedade será associada a um **Mod ID**, por exemplo `create`, `mekanism` ou `botania`.

Para identificar o proprietário de um item ou bloco, utilizar o namespace do seu identificador de registro, e não o nome exibido, nome traduzido ou nome da receita.

Exemplo conceitual:

```text
create:andesite_alloy  -> Mod ID create
mekanism:basic_control_circuit -> Mod ID mekanism
minecraft:iron_ingot -> Mod ID minecraft
```

Para receitas, a regra deve verificar o item de resultado efetivamente produzido. O namespace do ID da receita pode ser diferente do namespace do item de saída.

### 4.2 Atribuição de proprietário

No MVP, a atribuição será administrativa. O administrador define qual UUID é o proprietário de cada Mod ID. Não confiar no nome do jogador como identificador persistente.

Requisitos:

- Armazenar UUID do proprietário.
- Exibir nome atual/resolvido do jogador apenas para apresentação.
- Permitir alterar e remover proprietário.
- Definir comportamento claro quando o proprietário não estiver online.
- Permitir bypass administrativo configurável.
- Não permitir que um jogador abra o gerenciador para um mod que não possui.

**Proposta MVP:** um proprietário principal por Mod ID. Delegados e coproprietários ficam para etapa posterior.

### 4.3 Comandos administrativos mínimos

Criar comandos equivalentes aos exemplos abaixo, adequando a sintaxe à API do loader:

```text
/modlicense owner set <mod_id> <player>
/modlicense owner remove <mod_id>
/modlicense owner list
/modlicense owner info <mod_id>
/modlicense licenses [player]
/modlicense license revoke <mod_id> <player> [permission]
/modlicense license clear <mod_id> <player>
/modlicense ticket inspect
/modlicense reload
```

Comandos que alteram propriedade ou revogam licenças exigem permissão administrativa. Consultas públicas podem ser limitadas por configuração.

### 4.4 Política para mods sem proprietário

Comportamento padrão: **mods sem proprietário continuam livres**, sem bloqueios adicionais. Não bloquear todo o modpack por ausência de configuração.

---

## 5. Modelo de permissões

### 5.1 Permissões do MVP

A lista precisa ser extensível, mas o MVP deve começar com permissões cujo significado possa ser explicado e testado.

| Código | Nome exibido | Significado inicial |
|---|---|---|
| `CRAFT` | Fabricação | Retirar/produzir o resultado de crafting manual protegido em uma ação atribuída ao jogador. |
| `USE` | Utilização | Usar item na mão ou executar ação de uso coberta por evento genérico. |
| `PLACE` | Colocação | Colocar no mundo um bloco/item cujo namespace é protegido. |
| `INTERACT` | Interação | Interagir com blocos/bloco-entidades protegidos, como abrir/operar máquinas quando a ação é interceptável. |
| `BREAK` | Quebra | Quebrar blocos protegidos. |

`CRAFT`, `USE`, `PLACE`, `INTERACT` e `BREAK` são permissões independentes. Um ticket pode conter uma ou várias.

### 5.2 Permissões futuras

Planejar espaço para `DROP`, `PICKUP` e `POSSESSION`, mas não anunciar posse completa no MVP. Posse é uma regra mais ampla e difícil de garantir porque itens podem entrar em inventários por máquinas, baús, comandos, recompensas e mods de armazenamento.

### 5.3 Semântica obrigatória

- `USE` não deve ser apresentado como “qualquer ação possível no Minecraft”. Deve cobrir apenas os pontos de uso que o adaptador intercepta e testa.
- `INTERACT` deve proteger interações iniciadas pelo jogador; não garante bloquear processos internos automatizados de máquinas sem integração específica.
- `CRAFT` no MVP cobre crafting manual suportado pela API do loader. Máquinas de processamento de terceiros ficam fora do bloqueio universal.
- `PLACE` e `BREAK` devem ser avaliados no servidor antes de confirmar a alteração do mundo.
- Uma permissão concedida para um Mod ID só vale para itens/blocos daquele namespace, salvo regras explícitas futuras.

### 5.4 Resolução de permissão

Criar um serviço central semelhante conceitualmente a:

```java
PermissionDecision checkPermission(
    UUID playerId,
    String modId,
    LicensePermission permission,
    ActionContext context
);
```

O adaptador determina o Mod ID e a ação. O serviço central decide se o jogador tem licença ativa, se ela inclui a permissão, se expirou e se existe bypass aplicável.

Retornos precisam distinguir pelo menos:

- `ALLOW`: ação autorizada por licença;
- `DENY`: ação bloqueada por falta/expiração de licença;
- `EXEMPT`: item/bloco consta na lista administrativa de exceções e deve ignorar toda validação de licença aplicável àquele registro;
- `NOT_APPLICABLE`: item/mod sem proprietário ou ação não controlada pelo sistema.

O adaptador deve tratar `ALLOW`, `EXEMPT` e `NOT_APPLICABLE` como ações não bloqueadas, respeitando a semântica específica de cada retorno. Não tratar `NOT_APPLICABLE` como erro. `EXEMPT` deve ser preservado no log de diagnóstico para distinguir uma exceção explícita de um item que simplesmente não é protegido.

---

## 6. Livro de Autorização e atril

### 6.1 Livro

Registrar um item próprio, por exemplo `authorization_book`, utilizando a aparência vanilla de livro sempre que possível. Não é necessário criar uma textura de livro customizada no MVP.

O Livro de Autorização é uma ferramenta reutilizável do proprietário. Não é o ticket concedido ao comprador.

### 6.2 Interação com atril

Fluxo pretendido:

1. O proprietário segura o Livro de Autorização.
2. Clica com o botão direito em um atril vanilla.
3. O servidor valida que o item é o Livro de Autorização e que o jogador possui ao menos um Mod ID atribuído.
4. O servidor abre o menu personalizado.
5. A interface lista apenas os mods que pertencem ao jogador.
6. O jogador escolhe um mod, as permissões, a validade e as opções permitidas.
7. O servidor valida novamente todas as seleções e emite o pergaminho.

**Implementação preferida para o MVP:** interceptar o clique com o Livro de Autorização na mão e usar o atril como estação de trabalho, sem substituir a entidade/bloco vanilla e sem exigir que o livro seja inserido no inventário interno do atril. Isso reduz risco de conflito com o funcionamento vanilla. Se houver uma razão técnica para inserir o livro no slot real do atril, documentar e testar o impacto sobre livros escritos normais.

### 6.3 Interface

A interface deve manter linguagem visual próxima às telas vanilla:

- fundo fornecido pelo criador do projeto;
- título “Livro de Autorização”;
- seleção de Mod ID com nome e identificador;
- lista de permissões com checkbox;
- seleção de validade;
- opção de ticket público/transferível ou vinculado, quando implementada;
- prévia visual do pergaminho;
- botão para emitir;
- área de feedback/erro.

Requisitos de segurança da interface:

- O cliente apenas envia a intenção de configuração.
- O servidor confere propriedade, permissões selecionadas, validade e capacidade de emitir.
- Não aceitar um Mod ID arbitrário enviado pelo cliente sem conferir que o jogador é proprietário.
- Não confiar no estado local da tela para alterar dados persistentes.
- Fechar/desconectar/reabrir a interface não pode duplicar uma emissão.
- Bloquear emissão se nenhuma permissão estiver selecionada.
- Bloquear emissão se o inventário não tiver espaço ou disponibilizar saída segura no menu, sem apagar o item.

---

## 7. Pergaminho de licença

### 7.1 Um item registrado, várias aparências

Registrar **um único Item ID**, por exemplo `license_scroll`.

A cor do laço é escolhida conforme as permissões armazenadas no item. Não registrar um item distinto por cor. Usar os dados do item e o sistema de modelos/condições adequado à versão para selecionar a textura/modelo correto.

Aparências previstas:

| Aparência | Significado |
|---|---|
| Laço vermelho | `CRAFT` |
| Laço azul | `USE` |
| Laço verde | `PLACE` |
| Laço dourado/amarelo | `INTERACT` ou outra permissão que o criador definir antes dos assets |
| Laço laranja | `BREAK` |
| Laço roxo | Duas ou mais permissões (`MULTI`) |

A paleta é uma proposta inicial e deve ser congelada no `ASSET-MANIFEST.md` antes de integrar as imagens finais. Não alterar a semântica das cores sem atualizar a documentação.

Para `MULTI`, usar pergaminho com laço roxo. A tooltip deve mostrar a lista completa de permissões; a cor sozinha não é suficiente para identificar o pacote.

### 7.2 Dados do ticket

Cada pergaminho precisa carregar dados estruturados, não apenas nome/lore. Campos conceituais:

```text
schemaVersion
serialId                 // UUID ou identificador único imprevisível
modId
permissions[]
issuerUuid
issuedAt
validityType             // PERMANENT ou TEMPORARY
validityDuration         // se temporária
boundPlayerUuid          // opcional; vazio para ticket transferível
```

A implementação concreta deve usar a solução adequada ao alvo (componentes de item em versões modernas; abstração/serialização específica em versões antigas). Não assumir que o formato interno de dados é idêntico entre versões.

### 7.3 Transferência e venda

**Padrão do MVP:** ticket público e transferível. Qualquer jogador pode carregar, guardar, vender ou trocar antes do resgate.

O ticket deve ser não empilhável (`maxStackSize = 1`) enquanto tiver serial único, para evitar ambiguidade entre unidades com dados diferentes. Máquinas de venda normalmente conseguem vender uma unidade de um item não empilhável; validar isso em uma integração real.

Ticket vinculado a UUID é futuro. Quando implementado, apenas o jogador indicado pode resgatá-lo.

### 7.4 Proteção contra cópia e falsificação

Não confiar apenas nos dados do ItemStack. Um jogador pode duplicar ou alterar NBT/componentes com comandos, modo criativo, bugs ou ferramentas administrativas.

Criar um registro autoritativo persistente no servidor para cada ticket emitido:

- serialId;
- Mod ID;
- permissões;
- emissor;
- estado (`ISSUED`, `REDEEMED`, `REVOKED`);
- timestamps;
- UUID do resgatador, quando resgatado.

No resgate, validar no servidor que:

1. o serial existe no registro;
2. o estado é `ISSUED`;
3. os dados do item correspondem ao registro;
4. o destinatário confere, se o ticket for vinculado;
5. o ticket não foi revogado;
6. a licença ainda pode ser concedida conforme as regras do servidor.

A alteração `ISSUED -> REDEEMED` e a concessão da licença precisam ser atômicas na lógica do servidor: não permitir dois resgates simultâneos do mesmo serial.

Tickets inválidos, já resgatados ou falsificados não concedem licença. Não consumir silenciosamente um ticket legítimo por uma falha temporária de sincronização.

### 7.5 Tooltip

Exibir:

- nome “Pergaminho de Licença”;
- mod alvo (nome amigável e Mod ID);
- permissões incluídas;
- emissor;
- validade;
- indicação “Transferível” ou destinatário, se aplicável;
- aviso para licença temporária, quando aplicável.

Não mostrar serial completo ao jogador comum; disponibilizar em comando de inspeção/admin.

---

## 8. Resgate e ativação da licença

### 8.1 Fluxo de resgate

1. Jogador usa o pergaminho com botão direito.
2. O servidor valida o item e o serial.
3. O servidor valida destinatário e estado do ticket.
4. O servidor cria/atualiza a licença para `player UUID + modId`.
5. O servidor registra o ticket como resgatado.
6. O servidor consome exatamente uma unidade do pergaminho.
7. O servidor sincroniza o resultado com o cliente.
8. O jogador recebe feedback visual, sonoro e textual.

Se qualquer validação falhar antes da confirmação, o item não deve desaparecer.

### 8.2 Licenças permanentes e temporárias

MVP: licença permanente.

Preparar modelo de dados para validade temporária, mas não habilitar na primeira entrega sem testes de persistência e expiração. Proposta futura: a duração começa no momento do resgate, e não na emissão/venda, para que o tempo de exposição na máquina de venda não consuma a licença.

### 8.3 Armazenamento de licenças

Chave lógica recomendada:

```text
playerUuid + modId -> LicenseGrant
```

O registro deve permitir guardar várias permissões, validade, emissor, data de concessão e serial de origem.

Se o jogador receber uma segunda licença do mesmo Mod ID:

- unir permissões sem apagar as já ativas;
- não reduzir uma validade maior por causa de uma licença menor;
- definir comportamento claro para múltiplos prazos (preferência inicial: armazenar grants separados e calcular autorização se ao menos um grant válido contiver a permissão).

A revogação deve poder remover uma permissão específica ou todas as permissões daquele mod para o jogador.

### 8.4 Animação e som

Usar recursos vanilla para manter a identidade visual:

- partículas verdes/douradas semelhantes a `HAPPY_VILLAGER` e/ou partículas de experiência;
- som vanilla de evolução/celebração adequado à versão;
- animação de uso do item semelhante a consumir/comer, se aplicável ao item;
- mensagem de confirmação listando mod e permissões ativadas.

O agente deve confirmar os identificadores reais de partículas e sons no alvo escolhido. Não adicionar arquivos de som no MVP.

A sequência ocorre somente após a concessão ter sido confirmada pelo servidor. Não tocar animação de sucesso se o ticket for inválido.

---

## 9. Motor de proteção e integrações com o jogo

### 9.1 Regra central

Toda tentativa de ação protegida deve passar pelo serviço central de autorização. Cada adaptador recebe um evento de jogo, identifica o jogador, o item/bloco e o Mod ID, e pergunta ao serviço se a ação está autorizada.

**Ordem obrigatória da verificação:**

1. Resolver o ID registrado exato do item ou bloco envolvido na ação.
2. Consultar a lista de exceções carregada pelo servidor. Se o ID estiver listado na seção correspondente (`ignoredItems` ou `ignoredBlocks`), retornar `EXEMPT` e não exigir licença para aquela ação.
3. Se não estiver isento, resolver o Mod ID e seu proprietário.
4. Se o mod não tiver proprietário, retornar `NOT_APPLICABLE` (padrão: livre).
5. Caso tenha proprietário, verificar bypass administrativo e, em seguida, a licença/permissão do jogador conforme a política de bypass definida.
6. Retornar `ALLOW` ou `DENY`.

A exceção deve prevalecer sobre a existência de proprietário e sobre a ausência de licença: um item explicitamente listado continua livre mesmo que pertença a um mod protegido. A lista é definida exclusivamente pelo administrador no servidor; nunca aceitar exceções enviadas pelo cliente.

Não criar regras duplicadas espalhadas em diversos event handlers.

### 9.2 Matriz mínima de cobertura

O agente deve preencher e manter esta matriz durante o desenvolvimento:

| Ação | Fonte de verificação | Escopo garantido do MVP | Observação |
|---|---|---|---|
| Craft manual | Evento/pipeline de retirada do resultado | Resultado de receita manual associado a ação do jogador | Verificar o ponto correto antes de conceder o item. Não usar apenas evento posterior ao craft. |
| Colocar bloco | Evento de colocação no servidor | Colocação iniciada por jogador | Bloquear antes da alteração do mundo. |
| Quebrar bloco | Evento de quebra no servidor | Quebra iniciada por jogador | Considerar ferramentas e creative mode conforme configuração. |
| Usar item | Evento de interação/uso | Interações genéricas documentadas | Testar comida, ferramentas, armas e itens com uso especial. |
| Interagir com bloco | Evento de clique/interação | Interações iniciadas por jogador em blocos protegidos | Pode exigir validação antes do bloco de destino processar a interação. |
| Drop | Evento de descarte | Futuro ou opcional | Não equivale a posse. |
| Pickup | Evento de coleta | Futuro ou opcional | Não cobre todos os caminhos de transferência. |
| Inserir em máquina | Integração específica | Fora da garantia genérica | Exige API/evento próprio da máquina/mod. |
| Retirar de máquina | Integração específica | Fora da garantia genérica | Exige identificação do jogador e contexto no mod externo. |

### 9.3 Máquinas automatizadas

Este ponto é crítico para a comunicação do produto.

Em máquinas automatizadas, muitas operações acontecem por tick, capability, inventário ou transferência de itens, sem um jogador diretamente associado à produção. O mod não deve inventar uma identidade de produtor.

Estratégia do MVP:

- não afirmar que `CRAFT` bloqueia universalmente a produção em máquinas de terceiros;
- permitir que o usuário bloqueie `INTERACT`, `USE`, `PLACE` ou `BREAK` quando o evento genérico consegue identificar a ação do jogador;
- documentar que inserção/retirada automatizada por pipes, funis ou máquinas pode continuar ocorrendo;
- criar integrações específicas somente quando houver um evento/API público capaz de identificar o jogador e/ou a operação.

### 9.4 Integrações futuras

Para cada integração (ex.: Create, Mekanism, Applied Energistics 2):

1. verificar licença, versão e API pública do mod;
2. identificar exatamente os pontos em que jogador inicia interação, insere ou retira itens;
3. confirmar se o evento ocorre antes da ação e pode ser cancelado;
4. adicionar integração opcional, carregada somente quando o mod está presente;
5. testar com o mod e versão correspondentes;
6. documentar o que está coberto e o que não está.

Não adicionar dependência obrigatória desses mods. A ausência deles não pode impedir a inicialização do ModLicense.

### 9.5 JEI/EMI/REI

A exibição de receitas bloqueadas é uma integração opcional P2. Não esconder receitas silenciosamente no MVP, a menos que exista uma solução genérica e segura. A interface de receitas deve indicar que a permissão está ausente e, se possível, identificar o proprietário. Cada integração deve ser opcional e não pode virar dependência obrigatória.

---

## 10. Persistência, rede e segurança

### 10.1 Dados persistentes

Persistir pelo menos:

- mapeamento Mod ID -> proprietário UUID;
- registros de tickets emitidos e seu estado;
- licenças/grants por jogador;
- opções administrativas persistentes;
- versão do schema de dados.

Usar o mecanismo persistente apropriado ao loader/versão (dados salvos do mundo ou solução equivalente). Não depender de memória estática ou de dados somente no cliente.

### 10.2 Reinicialização e corrupção

- Dados devem sobreviver a reinício normal e crash controlado.
- Criar migração/versionamento de schema.
- Em erro de leitura, registrar mensagem clara e preservar backup quando possível.
- Nunca limpar todos os registros silenciosamente porque um campo novo não existe.
- Não gravar arquivos a cada tick; persistir em alterações relevantes e usar dirty/save mechanism do jogo.

### 10.3 Rede

- Pacotes cliente-servidor devem ser versionados e validados.
- Servidor valida cada mensagem recebida.
- Limitar tamanho de strings/listas enviadas pela interface.
- Não aceitar permissões, mod IDs ou UUIDs fornecidos pelo cliente sem autorização.
- Evitar sincronizar serials e dados internos de emissão para clientes que não precisam deles.

### 10.4 Bypass

Configuração administrativa para:

- bypass de OP/administradores;
- habilitar/desabilitar mensagens de bloqueio;
- habilitar/desabilitar verificação em creative mode;
- política de mods sem proprietário (padrão: livre);
- ativar/desativar categorias de proteção.

O bypass deve estar explicitamente documentado e testado.

### 10.5 Exceções administrativas por ID (JSON)

O administrador do servidor precisa poder excluir itens específicos do sistema de licenças, mesmo quando esses itens pertencem a um Mod ID que possui proprietário. Isso é necessário para itens utilitários, componentes básicos, itens de progressão, recompensas de quests, itens de integração entre mods ou qualquer exceção definida pelo modpack.

O MVP deve carregar uma configuração JSON mantida pelo servidor, no caminho: `config/modlicense/exceptions.json`. O mod deve criar o diretório e um arquivo inicial com arrays vazios na primeira inicialização. A configuração do servidor é a única fonte de autoridade; não ler a lista de exceções do cliente para tomar decisões de permissão.

**Formato proposto:**

```json
{
  "schemaVersion": 1,
  "ignoredItems": [
    "modid:basic_component",
    "anothermod:quest_reward"
  ],
  "ignoredBlocks": [
    "modid:decorative_machine"
  ]
}
```

**Semântica:**

- `ignoredItems`: IDs completos de itens registrados (`namespace:path`). Quando um item da lista estiver envolvido em uma ação coberta, ele ignora a validação de licença para esse item, incluindo `CRAFT`, `USE` e `PLACE` quando aplicáveis.
- `ignoredBlocks`: IDs completos de blocos registrados (`namespace:path`). Quando um bloco da lista for o alvo de uma ação, ele ignora a validação de licença para esse bloco, incluindo `INTERACT` e `BREAK` quando aplicáveis.
- As listas são independentes. Para isentar tanto o item quanto o bloco correspondente de uma forma explícita, adicionar o ID nas duas listas. Não presumir que todo item possui um bloco correspondente ou que os IDs de item e bloco são sempre iguais.
- Comparar IDs completos e exatos, incluindo namespace. Não aceitar apenas `path`, curingas, regex ou uma entrada que represente o Mod ID inteiro no MVP.
- IDs desconhecidos no modpack atual devem gerar aviso no log, mas permanecer no arquivo para que a configuração continue válida caso o mod correspondente seja instalado novamente.
- IDs inválidos devem gerar aviso e ser ignorados; não devem derrubar a inicialização do servidor.
- Remover duplicatas em memória, sem reescrever/destruir a configuração original desnecessariamente.
- A lista deve ser aplicada antes de consultar proprietário/licença. Uma exceção explícita sempre libera a ação coberta, mesmo que o mod tenha proprietário e o jogador não possua ticket.
- A lista não concede uma licença ao jogador e não gera ticket; apenas exclui os registros listados da fiscalização.
- Não permitir que o Livro de Autorização nem um proprietário de mod alterem essa lista. Somente o administrador que controla os arquivos/configuração do servidor pode editá-la.

**Recarga:**

- Carregar no início do servidor e disponibilizar `/modlicense reload` para recarregar a configuração.
- A recarga deve validar o novo arquivo antes de substituir a configuração ativa.
- Se o JSON estiver malformado, registrar erro claro, manter a última configuração válida em memória (ou, na primeira inicialização, usar listas vazias) e não transformar o erro em uma liberação geral de permissões.
- Não aceitar caminho de arquivo fornecido pelo cliente ou por argumentos arbitrários do comando.

**Exemplo de comportamento esperado:**

Se `create` tiver o Jogador A como proprietário, mas `create:andesite_alloy` estiver em `ignoredItems`, qualquer jogador poderá fabricar, usar e movimentar esse item nas ações cobertas, sem precisar de licença do Create. Outros itens do Create continuam sujeitos às permissões normalmente. Se `create:andesite_casing` estiver apenas em `ignoredBlocks`, os jogadores poderão interagir/quebrar o bloco conforme a cobertura implementada, mas isso não isenta automaticamente um item de inventário com o mesmo nome.

A configuração deve ser documentada no README e incluída nos testes de unidade e de servidor dedicado.

---

## 11. Assets que serão fornecidos pelo criador do projeto

**O agente é responsável por criar a estrutura de pastas, registrar os recursos e conectá-los ao código. O criador fornecerá os PNGs finais.** Não redesenhar, recolorir ou substituir os arquivos fornecidos sem autorização.

Namespace de recursos provisório: `modlicense`.

### 11.1 Arquivos necessários para o MVP

| Arquivo | Uso | Dimensão/formato sugerido |
|---|---|---|
| `license_scroll_craft.png` | Pergaminho com laço vermelho | PNG RGBA, 16×16 px, pixel art |
| `license_scroll_use.png` | Pergaminho com laço azul | PNG RGBA, 16×16 px, pixel art |
| `license_scroll_place.png` | Pergaminho com laço verde | PNG RGBA, 16×16 px, pixel art |
| `license_scroll_interact.png` | Pergaminho com laço dourado/amarelo | PNG RGBA, 16×16 px, pixel art |
| `license_scroll_break.png` | Pergaminho com laço laranja | PNG RGBA, 16×16 px, pixel art |
| `license_scroll_multi.png` | Pergaminho com laço roxo, para múltiplas permissões | PNG RGBA, 16×16 px, pixel art |
| `license_lectern.png` | Fundo da interface do Livro de Autorização | PNG RGBA, 256×256 px; área útil central sugerida de 176×166 px |

O livro de autorização deve reutilizar o modelo/textura vanilla do livro sempre que viável. Não criar `authorization_book.png` a menos que o criador solicite uma identidade visual diferente.

### 11.2 Diretório esperado no projeto

```text
forge/src/main/resources/assets/modlicense/textures/item/
├── license_scroll_craft.png
├── license_scroll_use.png
├── license_scroll_place.png
├── license_scroll_interact.png
├── license_scroll_break.png
└── license_scroll_multi.png

forge/src/main/resources/assets/modlicense/textures/gui/
└── license_lectern.png

neoforge/src/main/resources/assets/modlicense/textures/item/
├── license_scroll_craft.png
├── license_scroll_use.png
├── license_scroll_place.png
├── license_scroll_interact.png
├── license_scroll_break.png
└── license_scroll_multi.png

neoforge/src/main/resources/assets/modlicense/textures/gui/
└── license_lectern.png
```

Se o projeto usar recursos compartilhados via Gradle, manter uma única pasta-fonte e copiar/incluir os assets nos dois JARs. Não manter cópias divergentes.

### 11.3 Regras de qualidade dos assets

- PNG com transparência quando necessário.
- Pixel art sem anti-aliasing ou blur.
- Todas as variações de pergaminho devem ter a mesma silhueta, posição, escala e iluminação; somente o laço/detalhe de cor deve mudar.
- O laço precisa ser distinguível no tamanho normal de inventário.
- Não usar texto pequeno incorporado ao item.
- A variação `multi` deve ser visualmente distinta, mas ainda parecer o mesmo pergaminho.
- O fundo da interface deve respeitar os limites de corte e a área útil acordados com o implementador. O agente deve fornecer ao criador uma prévia da tela com medidas de posicionamento antes de congelar o asset.

### 11.4 Contrato de integração dos assets

O agente deve:

1. criar um `docs/ASSET-MANIFEST.md`;
2. documentar nome, dimensão, local e função de cada arquivo;
3. implementar modelos/definições de item para selecionar a textura com base nas permissões;
4. manter um único registro de item para o pergaminho;
5. criar um fallback visual temporário usando textura vanilla enquanto algum PNG ainda não foi entregue;
6. validar os assets no inventário, mão, tooltip e interface;
7. nunca deixar referência de textura quebrada no JAR final.

### 11.5 Recursos que não precisam de asset customizado no MVP

- Livro de Autorização: aparência vanilla.
- Partículas: partículas vanilla.
- Som: som vanilla.
- Fonte: fonte vanilla.
- Ícones de permissões na GUI: preferir texto/ícones vanilla, salvo aprovação de assets adicionais.

---

## 12. Interface, mensagens e localização

### 12.1 Textos mínimos

`pt_br` e `en_us` devem incluir:

- Livro de Autorização;
- Pergaminho de Licença;
- Fabricação, Utilização, Colocação, Interação e Quebra;
- Licença permanente/temporária;
- Mod ID e proprietário;
- botão Emitir Pergaminho;
- falta de permissão para gerenciar aquele mod;
- licença ativada;
- pergaminho inválido, revogado ou já resgatado;
- inventário cheio;
- ação bloqueada e motivo;
- licença revogada/expirada.

Não deixar textos de interface hardcoded no Java. Usar arquivos de tradução.

### 12.2 Mensagens de bloqueio

Mensagem sugerida em português:

```text
Esta ação está protegida pelo proprietário de {mod_name}.
Você precisa de uma licença de {permission}.
```

Se o proprietário for conhecido, pode exibir o nome dele. Não revelar informações administrativas sensíveis.

### 12.3 Mensagem após resgate

```text
Licença ativada: {mod_name}
Permissões concedidas: {permissions}
```

A mensagem deve ser enviada após a transação ter sido confirmada no servidor.

---

## 13. Fases de desenvolvimento e entregáveis

O agente deve trabalhar em etapas pequenas, manter o build funcional sempre que possível e não avançar quando os critérios de aceite da fase atual falharem.

### Fase 0 — Verificação técnica e plano de compatibilidade

**Tarefas**

1. Confirmar os MDKs e versões estáveis de Forge e NeoForge para Minecraft 1.21.1.
2. Confirmar JDK, Gradle e plugins recomendados.
3. Identificar APIs de eventos para crafting, interação, colocação, quebra, uso e descarte.
4. Identificar como abrir menus no servidor e sincronizar dados.
5. Identificar armazenamento persistente recomendado.
6. Registrar as limitações de cada loader.
7. Criar `docs/COMPATIBILITY.md` com a matriz de cobertura.

**Entregáveis**

- relatório técnico curto;
- versões fixadas;
- decisão de estrutura Gradle;
- lista dos eventos/APIs que serão usados e suas limitações.

**Aceite**

- ambos os alvos compilam com projeto mínimo;
- servidor dedicado inicia sem erros;
- nenhuma API é escolhida apenas por suposição ou código copiado de outra versão.

### Fase 1 — Esqueleto multi-loader

**Tarefas**

1. Criar os módulos common, Forge e NeoForge.
2. Registrar o mod ID `modlicense`.
3. Configurar runs de cliente e servidor.
4. Criar CI para build dos dois alvos.
5. Criar `README`, changelog inicial e convenções de código.

**Aceite**

- `build` bem-sucedido nos dois alvos;
- ambos carregam no cliente;
- ambos carregam em servidor dedicado;
- nenhum crash por referência a classes do cliente no servidor.

### Fase 2 — Modelo de domínio e persistência

**Tarefas**

1. Implementar `LicensePermission`.
2. Implementar modelos de proprietário, ticket e licença.
3. Implementar serviço de autorização puro.
4. Implementar armazenamento persistente por mundo/servidor.
5. Implementar versionamento/migração do schema.
6. Criar testes unitários do núcleo.

**Aceite**

- proprietário, ticket e licença sobrevivem a reinício;
- dados não dependem de jogador online;
- testes cobrem licença ausente, permissão concedida, permissão diferente, expiração simulada e revogação;
- um mod sem proprietário retorna `NOT_APPLICABLE` e permanece livre.

### Fase 3 — Administração e propriedade

**Tarefas**

1. Implementar comandos de proprietário.
2. Validar Mod ID contra a lista de mods carregados ou permitir ID conhecido com mensagem apropriada.
3. Armazenar UUID, não nome simples.
4. Implementar comandos de consulta e revogação.
5. Implementar bypass de OP configurável.

**Aceite**

- jogador comum não consegue atribuir propriedade;
- proprietário só gerencia Mod IDs atribuídos ao seu UUID;
- alteração de proprietário não corrompe licenças existentes;
- comandos dão mensagens claras para argumentos incorretos.

### Fase 4 — Livro e atril

**Tarefas**

1. Registrar `authorization_book` com aparência vanilla.
2. Interceptar o uso do livro ao clicar em atril vanilla.
3. Validar no servidor a propriedade do jogador.
4. Abrir Menu/Screen personalizado.
5. Listar apenas os mods pertencentes ao jogador.
6. Implementar seleção de permissões e prévia.
7. Implementar protocolo de rede validado.

**Aceite**

- livro normal no atril continua funcionando como vanilla;
- Livro de Autorização em outro bloco não abre o gerenciador;
- jogador sem propriedade não consegue emitir;
- cliente não consegue enviar outro Mod ID e emitir em nome de outro jogador;
- tela funciona em multiplayer real, não apenas single-player integrado.

### Fase 5 — Pergaminho e emissão

**Tarefas**

1. Registrar um único item `license_scroll`.
2. Criar dados estruturados e schema versionado para o item.
3. Criar serial único para cada ticket.
4. Registrar ticket emitido no servidor.
5. Definir cor/modelo a partir do conjunto de permissões.
6. Implementar tooltip completa.
7. Emitir para o inventário sem perda se estiver cheio.

**Aceite**

- existe um único item ID de pergaminho;
- todas as cores são variantes visuais do mesmo item;
- duas emissões geram serials diferentes;
- pergaminho transferido por baú/inventário continua válido;
- item alterado/serial inexistente é rejeitado;
- emissão duplicada por spam de pacote não gera cópias válidas extras.

### Fase 6 — Resgate da licença

**Tarefas**

1. Implementar uso com botão direito.
2. Validar ticket no servidor.
3. Fazer resgate atômico.
4. Consumir exatamente uma unidade.
5. Registrar licença pelo UUID do jogador e Mod ID.
6. Implementar revogação e consulta.
7. Implementar efeito visual/sonoro vanilla após sucesso.

**Aceite**

- ticket válido concede todas e somente as permissões nele descritas;
- ticket inválido não concede e não some silenciosamente;
- ticket resgatado não pode ser usado novamente;
- cópia de ticket com mesmo serial não dá segunda licença;
- licença continua ativa depois de reiniciar;
- licença revogada deixa de autorizar imediatamente após sincronização do servidor.

### Fase 7 — Motor de bloqueio

Implementar nesta ordem:

1. `PLACE`;
2. `BREAK`;
3. `INTERACT`;
4. `USE`;
5. `CRAFT` manual.

A ordem pode mudar caso a investigação técnica demonstre que outra ação é mais simples ou crítica, mas cada mudança deve ser registrada.

**Para cada ação:**

- identificar o evento/ponto de interceptação;
- provar que ocorre antes da ação ser concluída;
- cancelar no servidor;
- evitar duplicar a lógica de licença;
- informar o motivo ao jogador;
- criar teste positivo e negativo.

**Aceite**

- jogador com licença executa a ação;
- jogador sem licença é bloqueado;
- jogador com licença de outra permissão continua bloqueado;
- item vanilla e mod sem proprietário continuam funcionando;
- bypass administrativo funciona conforme configuração;
- exceção em `ignoredItems` ignora a licença somente para o ID exato listado;
- exceção em `ignoredBlocks` ignora a licença somente para o ID de bloco exato listado;
- um item não listado do mesmo mod continua bloqueado sem a licença adequada;
- não há duplicação/perda de itens quando ação é cancelada.

### Fase 8 — Compatibilidade visual e UX

**Tarefas**

1. Integrar assets finais fornecidos pelo criador.
2. Validar todas as variantes de laço.
3. Validar tela do atril em diferentes resoluções/gui scale.
4. Finalizar tooltips e traduções.
5. Ajustar animação e som.

**Aceite**

- nenhuma textura ausente;
- a cor sempre corresponde às permissões;
- `MULTI` aparece quando duas ou mais permissões estão configuradas;
- tooltip lista as permissões reais;
- textos não ficam cortados nas escalas suportadas.

### Fase 9 — Testes de modpack e release candidata

**Tarefas**

1. Testar Forge 1.21.1 e NeoForge 1.21.1 separadamente.
2. Testar servidor dedicado.
3. Testar com um mod tecnológico representativo, se disponível.
4. Testar ticket dentro de baú e máquina de venda de outro mod.
5. Testar save/restart e cópia de ticket.
6. Revisar crash logs e warnings relevantes.
7. Criar `CHANGELOG`, `README` e instruções de instalação.

**Aceite**

- ambos os JARs passam pela matriz de teste;
- não há dependências obrigatórias não declaradas;
- documentação descreve limites de bloqueio de máquinas;
- versão e loader estão claros no nome/metadados dos arquivos;
- release candidata tem hashes e arquivos reproduzíveis.

### Fase 10 — Publicação

**Tarefas**

1. Preparar páginas CurseForge, Modrinth e GitHub Releases.
2. Informar loader, versões Minecraft e ambiente (cliente/servidor).
3. Publicar descrição em inglês e português.
4. Adicionar imagens e tutorial fornecidos/aprovados pelo criador.
5. Não automatizar publicação sem tokens/segredos entregues pelo proprietário.
6. Não escolher licença de código/assets sem aprovação do proprietário.

**Aceite**

- arquivos corretos para cada loader;
- metadados e dependências conferidos;
- links de suporte e issue tracker funcionais;
- política de licença de código e assets documentada.

---

## 14. Plano de testes e validações

### 14.1 Testes unitários do domínio

- nenhum proprietário;
- proprietário atribuído;
- licença ausente;
- licença com a permissão necessária;
- licença com outra permissão;
- múltiplos grants para o mesmo Mod ID;
- revogação de uma permissão;
- revogação total;
- validade permanente;
- validade temporária (quando habilitada);
- ticket não emitido;
- ticket já resgatado;
- ticket revogado;
- ticket vinculado a outro UUID;
- combinação de permissões e seleção da cor correta;
- item listado em `ignoredItems` retorna `EXEMPT` mesmo que seu mod tenha proprietário;
- bloco listado em `ignoredBlocks` retorna `EXEMPT` para `INTERACT`/`BREAK`;
- item do mesmo mod não listado continua protegido;
- ID inválido ou desconhecido gera aviso e não derruba o servidor;
- JSON malformado não libera itens protegidos por acidente;
- recarregar JSON válido atualiza as exceções sem reiniciar o servidor.

### 14.2 Testes de jogo

- clique com Livro de Autorização em atril;
- clique com livro normal em atril;
- clique com Livro de Autorização em bloco diferente;
- emissão com inventário cheio;
- emissão de permissão única;
- emissão de duas ou mais permissões;
- consumo de ticket válido;
- consumo de ticket inválido;
- consumo duplicado;
- transferência por baú;
- transferência entre jogadores;
- transferência por máquina de venda compatível;
- desligar/reiniciar servidor e testar a licença novamente;
- revogar licença enquanto o jogador está online;
- tentativa de alterar os dados do item via comando;
- tentativa de pacote de rede com Mod ID não autorizado;
- editar `config/modlicense/exceptions.json`, executar `/modlicense reload` e verificar a nova regra;
- confirmar que cliente adulterado não consegue adicionar exceções nem sobrepor a lista do servidor.

### 14.3 Testes de bloqueio

Para cada permissão, testar:

- jogador sem licença;
- jogador com a licença correta;
- jogador com licença de outro Mod ID;
- jogador com licença do mesmo mod, mas outra permissão;
- operador com bypass;
- mod não protegido;
- item/bloco vanilla;
- ação em cliente multiplayer e validação final no servidor.

### 14.4 Teste de servidor dedicado obrigatório

Não aceitar apenas teste em single-player. A release exige pelo menos um teste em servidor dedicado para cada loader, confirmando:

- inicialização headless;
- conexão de cliente;
- emissão e resgate de ticket;
- sincronização de UI;
- bloqueio de ação no servidor;
- persistência após restart.

---

## 15. Qualidade de código e regras para o agente

O agente deve cumprir estas regras durante todo o desenvolvimento:

1. **Não implementar tudo em um único passo.** Trabalhar fase por fase e reportar o resultado de cada uma.
2. **Build verde como regra.** Após cada fase, executar build e testes relacionados.
3. **Não supor nomes de eventos/API.** Consultar a documentação/Javadocs da versão exata e testar se o evento é cancelável e ocorre antes da ação.
4. **Não inventar compatibilidade.** Se uma ação não puder ser interceptada genericamente, documentar como limitação e sugerir integração opcional.
5. **Não copiar código/assets de outros mods.** Projetos como MAM/Mod Access Manager e Crafting Patents podem ser estudados para comparar comportamento, mas código e assets só podem ser reutilizados se a licença permitir expressamente.
6. **Não adicionar dependências obrigatórias desnecessárias.** JEI, EMI, Create e máquinas de economia são integrações opcionais.
7. **Não confiar no cliente.** Propriedade, emissão, resgate, consumo do pergaminho e decisão de bloqueio são operações autoritativas do servidor.
8. **Não perder itens.** Emissão e resgate devem ser transacionais e cobrir inventário cheio, erro e spam de pacote.
9. **Não hardcodar textos.** Usar arquivos de idioma.
10. **Não criar assets finais.** Usar os arquivos fornecidos pelo criador ou fallback vanilla temporário.
11. **Documentar as limitações.** Especialmente crafting automatizado e interações internas de máquinas.
12. **Manter uma lista de decisões.** Registrar decisões de arquitetura em `docs/DECISIONS.md` com contexto, decisão e consequência.
13. **Não publicar automaticamente.** Gerar os artefatos e preparar metadados; a publicação depende da aprovação do proprietário e de credenciais adequadas.
14. **Não definir a licença legal do projeto por conta própria.** Deixar a escolha de licença do código e dos assets para o proprietário.

### 15.1 Relatório ao fim de cada fase

O agente deve entregar:

- resumo do que foi feito;
- arquivos principais criados/alterados;
- comandos executados;
- resultado do build e testes;
- limitações conhecidas;
- decisões pendentes;
- próxima fase recomendada.

---

## 16. Critérios finais de pronto para MVP

O MVP só está concluído quando todos os itens abaixo forem verdadeiros:

- [ ] Forge 1.21.1 compila e inicia cliente/servidor.
- [ ] NeoForge 1.21.1 compila e inicia cliente/servidor.
- [ ] Administrador consegue atribuir/remover proprietário de Mod ID.
- [ ] Proprietário abre o Livro de Autorização em um atril.
- [ ] Jogador não proprietário não consegue emitir licença para aquele mod.
- [ ] Interface permite escolher uma ou várias permissões.
- [ ] Emissão gera um único item `license_scroll` com serial único.
- [ ] Aparência do pergaminho corresponde às permissões escolhidas.
- [ ] Pergaminho pode ser transferido por inventário/baú e comercializado como item normal.
- [ ] Uso do pergaminho ativa a licença no UUID correto.
- [ ] Ticket não pode ser resgatado mais de uma vez.
- [ ] Licença e tickets sobrevivem ao restart do servidor.
- [ ] Comandos administrativos conseguem consultar e revogar licenças.
- [ ] O arquivo `config/modlicense/exceptions.json` é criado automaticamente e documentado.
- [ ] O administrador consegue isentar IDs exatos de itens/blocos sem alterar o proprietário do mod.
- [ ] `/modlicense reload` recarrega exceções com validação e sem liberar permissões por erro de JSON.
- [ ] Bloqueios P0 estão implementados somente onde existe interceptação segura e testada.
- [ ] Ações não cobertas estão documentadas como limitações.
- [ ] Efeito de ativação ocorre após o resgate confirmado.
- [ ] Não há assets quebrados nem textos sem tradução.
- [ ] Testes de servidor dedicado passam nos dois loaders.
- [ ] README explica instalação, comandos, permissões, limitações e configuração.

---

## 17. Fontes técnicas oficiais e referências para consulta

O agente deve priorizar documentação oficial da versão e Javadocs correspondentes, e não tutoriais antigos sem indicação de versão.

- [Forge Documentation — Getting Started](https://docs.minecraftforge.net/en/latest/gettingstarted/)
- [Forge Documentation — Events](https://docs.minecraftforge.net/en/latest/concepts/events/)
- [Forge Downloads — Minecraft 1.21.1](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.21.1.html)
- [NeoForge Documentation](https://docs.neoforged.net/)
- [NeoForge Documentation — Menus](https://docs.neoforged.net/docs/1.21.1/gui/menus/)
- [NeoForge Documentation — Events](https://docs.neoforged.net/docs/1.21.1/concepts/events/)
- [NeoForge Documentation — Item interaction pipeline](https://docs.neoforged.net/docs/1.21.1/items/interactions/)
- [NeoForge Documentation — Data Components](https://docs.neoforged.net/docs/1.21.1/items/datacomponents/)
- [NeoForge Documentation — Capabilities](https://docs.neoforged.net/docs/inventories/capabilities/)
- [NeoForge repository](https://github.com/neoforged/NeoForge)

### Projetos existentes para análise de paridade (não copiar implementação)

- [MAM — Mod Access Manager (CurseForge)](https://www.curseforge.com/minecraft/mc-mods/mam-mod-access-manager)
- [Crafting Patents (CurseForge)](https://www.curseforge.com/minecraft/mc-mods/crafting-patents)
- [SMP Utilities (Modrinth)](https://modrinth.com/project/smp-utilities)
- [Item Restrictions (CurseForge)](https://www.curseforge.com/minecraft/mc-mods/item-restrictions)

---

## 18. Primeira instrução para iniciar o agente

Ao receber este documento, **não comece implementando todas as funcionalidades**.

Comece pela Fase 0:

1. Leia o documento inteiro.
2. Verifique a situação atual dos MDKs Forge e NeoForge para Minecraft 1.21.1.
3. Proponha a estrutura Gradle multi-loader.
4. Confirme quais APIs/eventos permitem implementar cada permissão e marque os pontos que exigem integração específica.
5. Planeje a leitura, validação e recarga de `config/modlicense/exceptions.json`, garantindo que a lista seja autoritativa apenas no servidor.
6. Apresente a matriz de compatibilidade e as decisões pendentes.
7. Só depois de validar o plano, implemente a Fase 1.

O objetivo é produzir um mod estável, seguro e publicável, evitando prometer um bloqueio universal que a arquitetura de outros mods não permite garantir.
