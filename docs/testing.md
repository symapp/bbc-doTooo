# Testing

## Testcases

### Standart-Vorbedingungen

Aktuelle Version von Android Studio, funktionierender Android Emulator mit API 29 oder neuer.

### T-01

Vorbedingungen: Standart-Vorbedingungen

Ablauf:

1. Der Tester öffnet das Projekt in Android Studio und wartet bis das Projekt vollständig geladen ist.
2. Sobald das Projekt vollständig geladen ist, klickt den Tester auf den "Run" Button.

Erwartetes Resultat: Die App buildet ohne Errors (Warnings sind in Ordnung). Anschliessen installiert und öffnet sich die App auf dem Android Emulator. Am oberen Rand des Startbildschirms ist eine Actionbar mit dem Titel "DoTooo" und eine Lupe zu sehen. In der unteren rechten Ecke ist ein FloatingActionButton mit einem Plus zu sehen. Zudem ist ein Abschnitt mit dem Titel "Today" zu sehen. Rechts neben dem "Today" Titel wird der aktuelle Tag (z.B Freitag) angezeigt. Darunter ist ein Listenelement mit dem Schriftzug "No tasks" zu sehen. Unter diesem Abschnitt ist das gleiche nur für den morgigen Tag zu sehen.

### T-02

Vorbedingungen: Standart-Vorbedingungen, T-01 erfolgreich

Ablauf:

1. Falls die App noch nicht gestartet ist, startet der Tester die App wie bei T-01.
2. Anschliessend klickt der Tester auf den Button in der unteren rechten Ecke des Bildschirms.

Erwartetes Resultat: Es öffnet sich ein neuer Bildschirm mit dem ActionBar-Titel "Create Task". Es ist ein Textfeld mit dem Titel "Name", das heutige Datum, eine Auswahl mit der Standartauswahl "Grey", ein graues Viereck und ein Textfeld mit dem Titel "Description", ein blau ausgefüllter "Save"-Button und ein nicht ausgefüllter "Cancel"-Button zu sehen. Zudem befindet sich in der ActionBar links neben dem Titel ein Up-Button.

### T-03

Vorbedingungen: Standart-Vorbedingungen, T-03 erfolgreich

Ablauf:

1. Falls sich der Tester noch nicht auf dem "Create Task" Screen befindet, navigiert der Tester wie bei T-02 zu diesem.
2. 
