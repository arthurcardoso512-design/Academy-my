# ApexFit — Aplicativo de Musculação & Periodização Inteligente 🏋️‍♂️

Aplicativo completo de periodização, tracking de treino, temporizador de descanso, monitoramento de água, peso corporal e composição.

Disponível tanto como **App Android Nativo (Kotlin / Jetpack Compose)** quanto como **Web App Responsivo (HTML5 / PWA / GitHub Pages)**.

---

## 🚀 Como publicar no GitHub Pages (Gratuito)

1. Faça o envio (Push) deste repositório para o seu **GitHub**.
2. Acesse seu repositório no GitHub e vá na aba **Settings** (Configurações).
3. No menu lateral esquerdo, clique em **Pages**.
4. Em **Build and deployment** > **Branch**:
   - Selecione `main` (ou `master`).
   - Selecione a pasta `/(root)`.
   - Clique em **Save**.
5. Aguarde cerca de 1 minuto: seu link público no GitHub Pages estará pronto (ex: `https://seu-usuario.github.io/seu-repositorio/`).

---

## ✨ Funcionalidades Incluídas na Versão Web (`index.html`)

- **Divisão Periodizada ABCD**:
  - **Treino A**: Peito, Tríceps & Ombro Frontal (6 exercícios).
  - **Treino B**: Costas, Bíceps & Deltoide Posterior (6 exercícios).
  - **Treino C**: Pernas Completo & Panturrilhas (6 exercícios).
  - **Treino D**: Ombros Completos & Abdômen (6 exercícios).
- **Modo Treino Ativo**:
  - Cronômetro de sessão em tempo real.
  - Tabela interativa de séries (Carga em kg, Repetições, Checkmark).
  - Orientações de execução, foco muscular e alerta de postura.
  - RIR (Repetições em Reserva) alvo com guia explicativo.
- **Temporizador de Descanso Inteligente (HUD)**:
  - Contagem regressiva visual circular com ajustes rápidos (-15s, +15s, pausar).
  - Alerta sonoro ao zerar.
- **Evolução & Gráficos**:
  - Gráfico interativo de peso corporal em jejum (Chart.js).
  - Histórico de circunferências (Cintura, Abdômen, Tórax, Braço, Coxa).
- **Hábitos Diários & Metas**:
  - Contador e barra de progresso de ingestão de água (+250ml).
  - Contador de dias seguidos (streak).
- **Dark Mode & Light Mode**:
  - Alternância de tema com persistência automática no `localStorage`.
- **Backup & Restauração**:
  - Exportação e importação de dados em arquivo `.json`.
