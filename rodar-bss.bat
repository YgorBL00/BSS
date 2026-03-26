@echo off
REM Define o caminho da pasta onde o bat está
SET BASEDIR=%~dp0

REM Roda o JAR com JavaFX
java --module-path "%BASEDIR%javafx-sdk-21.0.10\lib" --add-modules javafx.controls,javafx.fxml -jar "%BASEDIR%target\BSS-standalone.jar"

REM Mantém o terminal aberto se der erro
pause