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

Vorbedingungen: Standart-Vorbedingungen, T-02 erfolgreich

Ablauf:

1. Falls sich der Tester noch nicht auf dem "Create Task" Screen befindet, navigiert der Tester wie bei T-02 zu diesem.
2. Der Tester gibt bei den Eingabefelder "Name" und "Description" etwas ein (z.B Test).
3. Der Tester wählt eine Farbe aus.
4. Der Tester klickt anschliessend auf den "Save" Button.
5. Der Tester verändert das Datum nicht

Erwartetes Resultat: Nach dem Klick auf den "Save" Button, wird der Tester zur Startseite weitergeleitet. Auf dieser ist bei dem Abschnitt "Today" nun das erstellte ToDo zu sehen.

### T-04

Vorbedingungen: Standart-Vorbedingungen, T-03 erfolgreich

Ablauf:

1. Falls noch kein ToDo erstellt wurde, erstellt der Tester ein ToDo wie bei T-03.
2. Der Tester klickt auf der Startseite auf das eben erstellte ToDo.

Erwartetes Resultat: Nach dem Klick auf das ToDo wird der Tester zur Detailseite des ToDo's weitergeleitet. Auf der Detailseite befinden sich in der ActionBar ein Pfeil, der Titel des ToDos, und drei Punkte. Unter der ActionBar ist ebenfalls der Titel des ToDo's zu sehen. Rechts neben dem Titel wird in einem Viereck die Farbe des ToDo's zu sehen. Unter dem Titel wird das Datum und die Description des ToDo's angezeigt. zudem wird ein grauer "Completed" Button angezeigt.

### T-05

Vorbedingungen: Standart-Vorbedingungen, T-04 erfolgreich

Ablauf:

1. Fals sich der Tester noch nicht auf dem DetailScreen befindet, navigiert der Tester zu diesem wie bei T-04
2. Der Tester klickt auf die drei Punkte und klickt anschliessend auf "Edit".

Erwartetes Resultat: Nach dem Klick auf den "Edit" Button wird der Tester zur Editierseite des ToDo's weitergeleitet. Auf dieser ist das selbe Formular wie beim erstellen eines ToDo's zu sehen. Jedoch sind die bereits vorhandenen Daten in das Formular eingefüllt.

### T-06

Vorbedingungen: Standart-Vorbedingungen, T-05 erfolgreich

Ablauf:

1. Falls sich der Tester noch nicht auf der Editierseite befindet, navigiert er zu dieser wie bei T-05.
2. Der Tester verändert beliebige Werte des ToDo's.
3. Anschliessend klickt der Tester auf den "Save" Button.
4. Der Tester klickt auf der Startseite das editierte ToDo an.

Erwartetes Resultat: Nach dem Klick auf den "Save" Button, wird der Tester zur Startseite weitergeleitet. Auf der Detailseite werden nun die editierten Werte korrekt angezeigt.

### T-07

Vorbedingungen: Standart-Vorbedingungen, T-04 erfolgreich

Ablauf:

1. Falls sich der Tester noch nicht auf der Detailseite befindet, navigiert er zu dieser wie bei T-04.
2. Der Tester klickt auf den grauen "Completed" Button.

Erwartetes Resultat: Nach dem Klick auf den "Completed" Button wird der Tester zur Startseite weitergeleitet. Auf dieser ist das ToDo's welches der Tester gerade completed hat nicht mehr zu sehen.
