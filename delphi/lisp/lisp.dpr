
program lisp;

{$APPTYPE CONSOLE}

uses
  SysUtils,
  Classes,
  ShellAPI, Windows, {for ShellExecute}
  classScanner in 'source\classScanner.pas',
  classStateMachine in 'source\classStateMachine.pas',
  classState in 'source\classState.pas',
  declarations in 'source\declarations.pas',
  classScannerIterator in 'source\classScannerIterator.pas',
  classParser in 'source\classParser.pas',
  classExpression in 'source\grammar\classExpression.pas',
  classMolecule in 'source\grammar\classMolecule.pas',
  classAtom in 'source\grammar\classAtom.pas',
  classNumber in 'source\grammar\classNumber.pas',
  classIdentifier in 'source\grammar\classIdentifier.pas',
  classCodeGenerator in 'source\classCodeGenerator.pas',
  classFunctionRegistry in 'source\classFunctionRegistry.pas';

{
  Replace .lisp with .asm in a string
    filename - *.lisp
    result - *.asm
}
function OutputFileName(filename: string): string;
var
  p: Integer;
begin
  p := Pos('.lisp', filename);
  if p = 0 then p := Length(filename);
  Result := Copy(filename, 1, p) + 'asm';
end;

{
  Create new process
}
procedure ExecNewProcess(ProgramName : String; Wait: Boolean);
var
  StartInfo : TStartupInfo;
  ProcInfo : TProcessInformation;
  CreateOK : Boolean;
begin
  FillChar(StartInfo,SizeOf(TStartupInfo),#0);
  FillChar(ProcInfo,SizeOf(TProcessInformation),#0);
  StartInfo.cb := SizeOf(TStartupInfo);
  CreateOK := CreateProcess(nil, PChar(ProgramName), nil, nil,False,
              CREATE_NEW_PROCESS_GROUP+NORMAL_PRIORITY_CLASS,
              nil, nil, StartInfo, ProcInfo);
  if CreateOK then
      if Wait then
        WaitForSingleObject(ProcInfo.hProcess, INFINITE);
  CloseHandle(ProcInfo.hProcess);
  CloseHandle(ProcInfo.hThread);
end;

{
  Main code
}
var
  inputfile, outputfile: string;
  lines: TStringList;
  scanner: TScanner;
  iter: TScannerIterator;
  parser: TParser;
  codegen: TCodeGenerator;
begin
  if ParamCount > 0 then
  try
    { calculate file names }
    inputfile := ParamStr(1);
    outputfile := OutputFileName(inputfile);

    { load input program }
    lines := TStringList.Create;
    lines.LoadFromFile(inputfile);
    lines[lines.Count-1] := lines[lines.Count-1] + #32;

    { scan for tokens }
    WriteLn('*** TOKENS ***');
    scanner := TScanner.Create(lines);
    scanner.Print;

    { parse program }
    iter := TScannerIterator.Create(scanner);
    parser := TParser.Create(iter);
    WriteLn(#13#10'*** PARSE TREE ***');
    parser.print;

    { generate ASM code }
    codegen := TCodeGenerator.Create(parser);
    codegen.Walk;

    { save output }
    codegen.Save(outputfile);

    { call assembler }
    ExecNewProcess('a86.com ' + outputfile, True);

    { collect garbage }
    codegen.Free;
    parser.Free;
    iter.Free;
    scanner.Free;
    lines.Free;
  except
    on E: Exception do
      WriteLn(E.Message);
    end
  else
    WriteLn('Error: no file name specified');

  WriteLn(#13#10'Done - press ENTER');
  Readln;
end.
