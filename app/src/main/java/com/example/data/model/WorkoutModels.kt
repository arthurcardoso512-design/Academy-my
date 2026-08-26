package com.example.data.model

data class ExerciseTemplate(
    val id: String,
    val name: String,
    val equipment: String,
    val defaultSets: Int,
    val minReps: Int,
    val maxReps: Int,
    val restSecondsMin: Int,
    val restSecondsMax: Int,
    val targetRir: String,
    val muscleFocus: String,
    val shortDescription: String,
    val executionTips: String,
    val alertWarning: String? = null,
    val alternativeExercise: String? = null
)

data class WorkoutTemplate(
    val code: String, // "A", "B", "C", "D"
    val title: String,
    val subtitle: String,
    val estimatedDurationMin: Int,
    val exercises: List<ExerciseTemplate>,
    val warmUpText: String,
    val cardioText: String,
    val isLocked: Boolean = false
)

object WorkoutCatalog {

    val TREINO_A = WorkoutTemplate(
        code = "A",
        title = "Treino A",
        subtitle = "Full Body — Base de força",
        estimatedDurationMin = 60,
        warmUpText = "5–7 minutos: 4–5 min de esteira ou bicicleta + 1–2 séries leves do primeiro exercício.",
        cardioText = "5–10 minutos de caminhada na esteira em ritmo moderado.",
        exercises = listOf(
            ExerciseTemplate(
                id = "a1_leg_press",
                name = "Leg Press 45°",
                equipment = "Máquina Leg Press 45°",
                defaultSets = 3,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 120,
                targetRir = "3",
                muscleFocus = "Quadríceps • Glúteos • Posteriores",
                shortDescription = "Controle a descida, mantenha os pés firmes na plataforma e evite perder o contato da lombar com o banco.",
                executionTips = "Posicione os pés na largura dos ombros. Desça até os joelhos formarem cerca de 90° sem arredondar a lombar. Empurre pelo calcanhar."
            ),
            ExerciseTemplate(
                id = "a2_supino_maquina",
                name = "Supino Máquina ou Halteres",
                equipment = "Máquina Chest Press / Halteres",
                defaultSets = 3,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 90,
                targetRir = "2–3",
                muscleFocus = "Peitoral • Tríceps • Deltoide anterior",
                shortDescription = "Mantenha as escápulas aduzidas e os pés firmes no chão durante todo o movimento.",
                executionTips = "Ajuste a altura do banco para que as pegadas fiquem na linha média do peitoral. Empurre sem travar os cotovelos bruscamente no final."
            ),
            ExerciseTemplate(
                id = "a3_puxada_frontal",
                name = "Puxada Frontal",
                equipment = "Polia alta (Lat Pulldown)",
                defaultSets = 3,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 90,
                targetRir = "2–3",
                muscleFocus = "Dorsal • Bíceps • Deltoide posterior",
                shortDescription = "Puxe a barra em direção ao peitoral superior, contraindo as costas e projetando o peito.",
                executionTips = "Evite balançar o tronco para trás excessivamente. Mantenha os cotovelos apontando para baixo durante a puxada."
            ),
            ExerciseTemplate(
                id = "a4_mesa_flexora",
                name = "Mesa Flexora",
                equipment = "Máquina Mesa Flexora Deitada",
                defaultSets = 2,
                minReps = 10,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 90,
                targetRir = "2",
                muscleFocus = "Isquiotibiais (Posterior de coxa)",
                shortDescription = "Mantenha o quadril firme contra o banco e flexione os joelhos com controle.",
                executionTips = "Ajuste o rolo logo acima dos calcanhares. Evite levantar o quadril do banco durante a contração máxima."
            ),
            ExerciseTemplate(
                id = "a5_desenvolvimento_maquina",
                name = "Desenvolvimento de Ombros na Máquina",
                equipment = "Máquina Shoulder Press",
                defaultSets = 2,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 60,
                restSecondsMax = 90,
                targetRir = "2–3",
                muscleFocus = "Deltoide (Ombros) • Tríceps",
                shortDescription = "Empurre com controle para cima, mantendo os cotovelos levemente à frente do tronco.",
                executionTips = "Ajuste o assento para que os punhos fiquem alinhados com a altura dos ombros. Não hiperestenda a lombar."
            ),
            ExerciseTemplate(
                id = "a6_triceps_polia",
                name = "Tríceps na Polia",
                equipment = "Polia alta com corda ou barra reta",
                defaultSets = 2,
                minReps = 10,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Tríceps (Braquial)",
                shortDescription = "Mantenha os cotovelos colados ao lado do corpo e estenda completamente os antebraços.",
                executionTips = "Mantenha os ombros parados. Apenas o antebraço se move para focar totalmente no tríceps."
            ),
            ExerciseTemplate(
                id = "a7_core",
                name = "Core (Pallof Press ou Abdominal Máquina)",
                equipment = "Polia / Máquina Abdominal",
                defaultSets = 2,
                minReps = 10,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Abdômen • Estabilidade do Core",
                shortDescription = "Mantenha o abdômen contraído e a postura ereta durante toda a execução.",
                executionTips = "Se fizer Pallof press, resista à rotação do tronco segurando 2 segundos na extensão máxima."
            )
        )
    )

    val TREINO_B = WorkoutTemplate(
        code = "B",
        title = "Treino B",
        subtitle = "Pernas + Costas",
        estimatedDurationMin = 60,
        warmUpText = "5–7 minutos de bicicleta ergométrica ou esteira.",
        cardioText = "5–10 minutos pós-treino.",
        exercises = listOf(
            ExerciseTemplate(
                id = "b1_agachamento_smith",
                name = "Agachamento no Smith",
                equipment = "Smith Machine",
                defaultSets = 3,
                minReps = 8,
                maxReps = 10,
                restSecondsMin = 90,
                restSecondsMax = 120,
                targetRir = "3",
                muscleFocus = "Quadríceps • Glúteos • Core",
                shortDescription = "Pés ligeiramente à frente da barra, coluna alinhada e descida controlada.",
                executionTips = "Apoie a barra sobre o trapézio, nunca nas vértebras cervicais. Desça até a coxa ficar paralela ao solo."
            ),
            ExerciseTemplate(
                id = "b2_remada_maquina",
                name = "Remada Máquina",
                equipment = "Máquina Seated Row",
                defaultSets = 3,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 90,
                targetRir = "2–3",
                muscleFocus = "Costas (Romboides, Dorsal) • Bíceps",
                shortDescription = "Apoie o peito no suporte, puxe com os cotovelos e aperte as escápulas atrás.",
                executionTips = "Puxe os cotovelos para trás junto ao corpo, sem elevar os ombros em direção às orelhas."
            ),
            ExerciseTemplate(
                id = "b3_supino_inclinado",
                name = "Supino Inclinado Máquina ou Halteres",
                equipment = "Banco Inclinado 30° / Máquina Inclinada",
                defaultSets = 3,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 90,
                targetRir = "2",
                muscleFocus = "Peitoral Superior • Deltoide Anterior",
                shortDescription = "Incline a cerca de 30–45°, descendo controlado até a altura do peito superior.",
                executionTips = "Mantenha o peito aberto e escápulas travadas para proteger as articulações dos ombros."
            ),
            ExerciseTemplate(
                id = "b4_cadeira_extensora",
                name = "Cadeira Extensora",
                equipment = "Máquina Extensora",
                defaultSets = 2,
                minReps = 10,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 90,
                targetRir = "2",
                muscleFocus = "Quadríceps",
                shortDescription = "Estenda os joelhos até a contração total e segure 1 segundo no topo.",
                executionTips = "Alinhe o eixo de rotação da máquina exatamente com a articulação do seu joelho."
            ),
            ExerciseTemplate(
                id = "b5_flexora",
                name = "Flexora (Sentada ou Deitada)",
                equipment = "Máquina Flexora",
                defaultSets = 2,
                minReps = 10,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 90,
                targetRir = "2",
                muscleFocus = "Posteriores de Coxa",
                shortDescription = "Controle o retorno do movimento para maximizar o trabalho dos isquiotibiais.",
                executionTips = "Evite fazer trancos ou usar impulso com a coluna."
            ),
            ExerciseTemplate(
                id = "b6_elevacao_lateral",
                name = "Elevação Lateral",
                equipment = "Halteres ou Polia",
                defaultSets = 2,
                minReps = 12,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Deltoide Lateral (Ombro)",
                shortDescription = "Eleve os braços lateralmente até a altura dos ombros com cotovelos levemente flexionados.",
                executionTips = "Não use o balanço do corpo. Pense em empurrar as mãos para os lados, não para cima."
            ),
            ExerciseTemplate(
                id = "b7_rosca_direta",
                name = "Rosca Direta ou Máquina",
                equipment = "Barra W / Halteres / Máquina Biceps",
                defaultSets = 2,
                minReps = 10,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Bíceps Braquial",
                shortDescription = "Flexione os cotovelos mantendo os braços estáveis ao lado do tronco.",
                executionTips = "Evite jogar os cotovelos para frente durante a subida para isolar o bíceps."
            )
        )
    )

    val TREINO_C = WorkoutTemplate(
        code = "C",
        title = "Treino C",
        subtitle = "Full Body + Condicionamento",
        estimatedDurationMin = 60,
        warmUpText = "5 minutos de aquecimento geral dinâmico e mobilidade articular.",
        cardioText = "8–10 minutos de esteira ou elíptico ao final do treino.",
        exercises = listOf(
            ExerciseTemplate(
                id = "c1_leg_press",
                name = "Leg Press 45°",
                equipment = "Máquina Leg Press 45°",
                defaultSets = 3,
                minReps = 10,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 90,
                targetRir = "2–3",
                muscleFocus = "Quadríceps • Glúteos",
                shortDescription = "Controle o movimento em amplitude confortável e segura.",
                executionTips = "Foco em cadência controlada (2 segundos descendo, 1 segundo empurrando)."
            ),
            ExerciseTemplate(
                id = "c2_puxada_frontal",
                name = "Puxada Frontal",
                equipment = "Polia Alta",
                defaultSets = 3,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 90,
                targetRir = "2",
                muscleFocus = "Costas • Dorsal",
                shortDescription = "Puxe a barra no peito abrindo a caixa torácica e contraia as costas.",
                executionTips = "Concentre-se em puxar com os cotovelos para ativar a musculatura dorsal."
            ),
            ExerciseTemplate(
                id = "c3_chest_press",
                name = "Chest Press / Supino Máquina",
                equipment = "Máquina Chest Press",
                defaultSets = 3,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 90,
                targetRir = "2",
                muscleFocus = "Peitoral • Tríceps",
                shortDescription = "Mantenha o tronco estável e empurre com força uniforme de ambos os braços.",
                executionTips = "Mantenha os ombros relaxados e longe das orelhas durante o empurrão."
            ),
            ExerciseTemplate(
                id = "c4_rdl_halteres",
                name = "Levantamento Terra Romeno (RDL)",
                equipment = "Halteres Médios",
                defaultSets = 2,
                minReps = 8,
                maxReps = 12,
                restSecondsMin = 90,
                restSecondsMax = 90,
                targetRir = "3",
                muscleFocus = "Glúteos • Isquiotibiais • Lombar",
                shortDescription = "Projete o quadril para trás mantendo os joelhos semi-flexionados e as costas retas.",
                executionTips = "Desça os halteres rente às pernas até sentir alongar a parte de trás das coxas.",
                alertWarning = "Priorize aprender o movimento. Não busque carga máxima.",
                alternativeExercise = "Flexora (caso a execução não esteja confortável)"
            ),
            ExerciseTemplate(
                id = "c5_remada_baixa",
                name = "Remada Baixa",
                equipment = "Polia Baixa com Triângulo",
                defaultSets = 2,
                minReps = 10,
                maxReps = 12,
                restSecondsMin = 60,
                restSecondsMax = 90,
                targetRir = "2",
                muscleFocus = "Costas Média e Inferior",
                shortDescription = "Mantenha a coluna ereta, estenda os braços e puxe em direção ao umbigo.",
                executionTips = "Mantenha os joelhos ligeiramente flexionados e contraia as costas no final do puxão."
            ),
            ExerciseTemplate(
                id = "c6_elevacao_lateral",
                name = "Elevação Lateral",
                equipment = "Halteres",
                defaultSets = 2,
                minReps = 12,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Deltoide Lateral",
                shortDescription = "Elevação suave até a altura dos ombros, focando na queimação muscular.",
                executionTips = "Não jogue o peso com a lombar. Mantenha o abdômen travado."
            ),
            ExerciseTemplate(
                id = "c7_panturrilha",
                name = "Panturrilha (Máquina ou Degrau)",
                equipment = "Máquina Panturrilha / Step",
                defaultSets = 2,
                minReps = 12,
                maxReps = 15,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Gastrocnêmio e Sóleo (Panturrilhas)",
                shortDescription = "Suba na ponta dos pés o máximo possível, segure 1 segundo no topo e desça alongando.",
                executionTips = "Realize uma pausa de 1 segundo embaixo para eliminar o efeito elástico do tendão."
            )
        )
    )

    val TREINO_D = WorkoutTemplate(
        code = "D",
        title = "Treino D",
        subtitle = "Cardio + Braços & Core (Opcional)",
        estimatedDurationMin = 45,
        warmUpText = "5 minutos de esteira em ritmo leve.",
        cardioText = "15–20 minutos de cardio moderado contínuo.",
        exercises = listOf(
            ExerciseTemplate(
                id = "d1_biceps",
                name = "Rosca Martelo",
                equipment = "Halteres",
                defaultSets = 3,
                minReps = 10,
                maxReps = 12,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Braquiorradial e Bíceps",
                shortDescription = "Pegada neutra com palmas voltadas uma para a outra.",
                executionTips = "Excelente para saúde dos antebraços e punhos."
            ),
            ExerciseTemplate(
                id = "d2_triceps_frances",
                name = "Tríceps Francês na Polia ou Halter",
                equipment = "Polia / Halter",
                defaultSets = 3,
                minReps = 10,
                maxReps = 12,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Cabeça Longa do Tríceps",
                shortDescription = "Braços estendidos acima da cabeça, flexionando cotovelos para trás.",
                executionTips = "Mantenha os cotovelos apontados para cima."
            ),
            ExerciseTemplate(
                id = "d3_prancha",
                name = "Prancha Abdominal",
                equipment = "Colchonete",
                defaultSets = 3,
                minReps = 30,
                maxReps = 45,
                restSecondsMin = 60,
                restSecondsMax = 60,
                targetRir = "2",
                muscleFocus = "Core e Estabilizadores",
                shortDescription = "Sustente o corpo alinhado apoiado nos antebraços e ponta dos pés.",
                executionTips = "Contraia glúteos e abdômen sem deixar a pelve afundar."
            )
        ),
        isLocked = true
    )

    val ALL_WORKOUTS = listOf(TREINO_A, TREINO_B, TREINO_C, TREINO_D)

    fun getWorkout(code: String): WorkoutTemplate {
        return ALL_WORKOUTS.find { it.code.equals(code, ignoreCase = true) } ?: TREINO_A
    }

    fun getNextWorkoutCode(lastCompletedCode: String?): String {
        return when (lastCompletedCode?.uppercase()) {
            "A" -> "B"
            "B" -> "C"
            "C" -> "A"
            "D" -> "A"
            else -> "A"
        }
    }
}
