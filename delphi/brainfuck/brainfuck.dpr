program brainfuck;

{$APPTYPE CONSOLE}

uses
  SysUtils, Classes;

const
  language: string = '><+-,.[]#';
  usage: string = 'Usage: brainfuck [-debug] filename[.bf]';
  notfound: string = 'File not found: ';
  debugmsg: string = 'line=%d col=%d instr=%s p=%d a[p]=%d';

type
  TCmdLineOptions = record
    filename: string;
    debug: boolean;
    fucked: boolean;
  end;

type
  TLineCol = record
    line: integer;
    col: integer;
  end;

var
  code: array of char;
  data: array of char;
  position: array of TLineCol;
  ptr: cardinal = 0;
  ip: cardinal = 0;
  codesize: cardinal = 0;
  datasize: cardinal = 30000;
  options: TCmdLineOptions;

procedure ParseParameters;
  var
    i: cardinal;
  begin
    for i := 1 to ParamCount do
    begin
      if ParamStr(i) = '-debug' then
        options.debug := True
      else
      begin
        options.filename := ParamStr(i);
        if not FileExists(options.filename) then
        begin
          if FileExists(options.filename + '.bf') then
            options.filename := options.filename + '.bf'
          else
          begin
            Writeln(ErrOutput,notfound + options.filename);
            options.fucked := True;
          end;
        end;
      end;
    end;
    if options.filename = '' then
    begin
      Writeln(ErrOutput,usage);
      options.fucked := True;
    end;
  end;

procedure ReadCode;
  var
    stringlist: TStringList;
    linenum,colnum: cardinal;
    instruction: cardinal;
    line: string;
  begin
    stringlist := TStringList.Create;
    stringlist.LoadFromFile(options.filename);
    for linenum := 0 to stringlist.Count - 1 do
    begin
      line := stringlist[linenum];
      for colnum := 1 to Length(line) do
      begin
        instruction := Pos(line[colnum],language);
        if instruction > 0 then
        begin
          SetLength(code,codesize+1);
          SetLength(position,codesize+1);
          code[codesize] := language[instruction];
          position[codesize].line := linenum + 1;
          position[codesize].col := colnum;
          Inc(codesize);
        end
      end;
    end;
    stringlist.Free;
  end;

procedure IncPtr;
  begin
    if ptr < datasize - 1 then
      Inc(ptr);
    Inc(ip);
  end;

procedure DecPtr;
  begin
    if ptr > 0 then
      Dec(ptr);
    Inc(ip);
  end;

procedure IncByte;
  begin
    if data[ptr] < #255 then
      Inc(data[ptr]);
    Inc(ip);
  end;

procedure DecByte;
  begin
    if data[ptr] > #0 then
      Dec(data[ptr]);
    Inc(ip);
  end;

procedure GetInput;
  var
    c: char;
  begin
    Read(Input,c);
    data[ptr] := c;
    Inc(ip);
  end;

procedure PutOutput;
  begin
    write(data[ptr]);
    Inc(ip);
  end;

procedure OpeningBracket;
  var
    ignore: integer;
  begin
    ignore := 0;
    if data[ptr] = #0 then
      while (code[ip] <> ']') or (ignore > 0) do
      begin
        Inc(ip);
        if ip = codesize then
          System.exit;
        if code[ip] = '[' then
          Inc(ignore);
        if (code[ip] = ']') and (ignore > 0) then
        begin
          Inc(ip);
          Dec(ignore);
        end;
      end;
    Inc(ip);
  end;

procedure ClosingBracket;
  var
    ignore: integer;
  begin
    ignore := 0;
    while (code[ip] <> '[') or (ignore > 0) do
    begin
      Dec(ip);
      if code[ip] = ']' then
        Inc(ignore);
      if (code[ip] = '[') and (ignore > 0) then
      begin
        Dec(ip);
        Dec(ignore);
      end;
    end;
  end;

procedure PrintDebug;
  var
    highest,i: integer;
  begin
    for i := datasize - 1 downto 0 do
      if data[i] > #0 then
      begin
        highest := i;
        break;
      end;
    for i := 0 to highest do
    begin
      Write(ErrOutput,Ord(data[i]));
      Write(ErrOutput,' ');
    end;
    WriteLn(ErrOutput,'');
    Inc(ip);
  end;

procedure Execute;
  var
    terminate: boolean;
  begin
    terminate := False;
    while not terminate do
    begin
      if options.debug then
        WriteLn(ErrOutput,Format(debugmsg,
          [position[ip].line,position[ip].col,code[ip],ptr,Integer(data[ptr])]
        ));
      case code[ip] of
        '>':  IncPtr;
        '<':  DecPtr;
        '+':  IncByte;
        '-':  DecByte;
        ',':  GetInput;
        '.':  PutOutput;
        '[':  OpeningBracket;
        ']':  ClosingBracket;
        '#':  PrintDebug;
      end;
      terminate := ip = codesize;
    end;
  end;

begin
  SetLength(code,codesize);
  SetLength(data,datasize);

  ParseParameters;

  if options.fucked then
    Exit;

  ReadCode;
  Execute;
end.
