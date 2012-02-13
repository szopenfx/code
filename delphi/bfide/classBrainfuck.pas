unit classBrainfuck;

interface

uses
  classStack;

const
  LANGUAGE = '-+<>[],.@';
type
  TBrainfuck = class(TObject)
    code: array of char;
    jump: array of integer;
    data: array[0..29999] of char;
    line: array of integer;
    col: array of integer;
    ip: integer;
    ptr: integer;
    codesize: integer;
    bytesread: integer;
    running: boolean;
    stop: boolean;
    stack: TStack;
    constructor Create;
    destructor Destroy; override;
    procedure AddCode(source:string);
    procedure Execute;
    procedure Run;
    procedure Reset;
    function IsReady: boolean;
  end;

var
  bf: TBrainfuck;

implementation

uses
  formMain, Forms;

constructor TBrainfuck.Create;
begin
  inherited Create;
  bf := Self;
  ip := 0;
  ptr := 0;
  bytesread := 0;
  codesize := 0;
  SetLength(code,0);
  SetLength(line,0);
  SetLength(col,0);
  SetLength(jump,0);
  stack := TStack.Create;
end;

destructor TBrainfuck.Destroy;
begin
  stack.Free;
  SetLength(code,0);
  SetLength(line,0);
  SetLength(col,0);
  bf := nil;
  inherited Destroy;
end;

procedure TBrainfuck.AddCode(source:string);
var
  i: integer;
  l,c: integer;
begin
  l := 1;
  c := 0;
  codesize := 0;
  stack.Clear;
  SetLength(code,0);
  SetLength(line,0);
  SetLength(col,0);
  SetLength(jump,0);
  for i := 1 to Length(source) do
  begin
    if source[i] > #32 then
      Inc(c);
    if source[i] = #13 then
    begin
      Inc(l);
      c := 0;
    end;
    if Pos(source[i],LANGUAGE) > 0 then
    begin
      SetLength(code,codesize+1);
      SetLength(line,codesize+1);
      SetLength(col,codesize+1);
      SetLength(jump,codesize+1);
      code[codesize] := source[i];
      if source[i] = '[' then
        stack.Push(codesize);
      if source[i] = ']' then
      begin
        jump[codesize] := stack.Pop;
        jump[jump[codesize]] := codesize;
      end;
      line[codesize] := l;
      col[codesize] := c;
      Inc(codesize);
    end;
  end;
end;

procedure TBrainfuck.Execute;
var
  old: char;
begin
  old := data[ptr];
  case code[ip] of
    '+':  if data[ptr] < #255 then Inc(data[ptr]);
    '-':  if data[ptr] > #0 then Dec(data[ptr]);
    '<':  if ptr > 0 then Dec(ptr);
    '>':  if ptr < 29999 then Inc(ptr);
    '[':  if data[ptr] = #0 then ip := jump[ip];
    ']':  if data[ptr] > #0 then ip := jump[ip];
    ',':  if bytesread < Length(frmMain.memInput.Text) then
          begin
            frmMain.Status.Panels[5].Text := '';
            Inc(bytesread);
            data[ptr] := frmMain.memInput.Text[bytesread];
          end
          else
          begin
            frmMain.Status.Panels[5].Text := 'Waiting for input';
            Application.ProcessMessages;
            Exit;
          end;
    '.':  frmMain.memOutput.Text := frmMain.memOutput.Text + data[ptr];
    '@':  stop := True;
  end;
  if old <> data[ptr] then
    if (not running) or frmMain.tbData.Down then
    frmMain.lvData.UpdateItems(ptr,ptr);
  Inc(ip);
end;

procedure TBrainfuck.Run;
begin
  running := True;
  while IsReady and not stop do
  begin
    Execute;
    frmMain.UpdateForm;
  end;
  stop := False;
  running := False;
end;

function TBrainfuck.IsReady: boolean;
begin
  result := ip < codesize;
end;

procedure TBrainfuck.Reset;
var
  i: integer;
begin
  for i := 0 to 29999 do
    data[i] := #0;
  ip := 0;
  ptr := 0;
  bytesread := 0;
end;

end.
