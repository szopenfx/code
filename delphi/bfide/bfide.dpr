program bfide;

uses
  Forms,
  formMain in 'formMain.pas' {frmMain},
  classBrainfuck in 'classBrainfuck.pas',
  classStack in 'classStack.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.Title := 'brainfuck IDE';
  Application.CreateForm(TfrmMain, frmMain);
  Application.Run;
end.
