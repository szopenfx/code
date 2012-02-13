unit classHighScores;
// class containing the 20 highest score objects

interface

uses
  Contnrs, ComCtrls,
  classScore;

type
  THighScores = class(TObject)
  private
    FScores: TObjectList;
    procedure SaveToFile;
    procedure ReadFromFile;
  public
    constructor Create;
    destructor Destroy; override;
    procedure AddScore(AScore:TScore);
    procedure ShowInListView(listview:TListView);
  end;

implementation

uses
  SysUtils, Forms, Classes, Dialogs, globals;

constructor THighScores.Create;
begin
  inherited Create;
  FScores := TObjectList.Create(True);
  ReadFromFile;
end;

destructor THighScores.Destroy;
begin
  SaveToFile;
  FScores.Free;
  inherited Destroy;
end;

procedure THighScores.AddScore(AScore:TScore);
var
  i: integer;
begin
  if AScore.Name = sDefaultName then
    AScore.Name := InputBox(sGameOver.cap,sGameOver.msg,AScore.Name);
  i := 0;
  while (i < FScores.Count) and (AScore.Cash < TScore(FScores.Items[i]).Cash) do
    Inc(i);
  FScores.Insert(i,AScore);
  SaveToFile;
end;

procedure THighScores.SaveToFile;
var
  f: textfile;
  i: integer;
begin
  AssignFile(f,ExtractFilePath(Application.ExeName) + 'scores.ini');
  Rewrite(f);
  for i := 0 to FScores.Count-1 do
    WriteLn(f,TScore(FScores.Items[i]).ToString);
  CloseFile(f);
end;

procedure THighScores.ReadFromFile;
var
  fn: string;
  f: textfile;
  s: string;
begin
  fn := ExtractFilePath(Application.ExeName) + 'scores.ini';
  if FileExists(fn) then
  begin
    AssignFile(f,fn);
    Reset(f);
    while not EOF(f) do
    begin
      ReadLn(f,s);
      FScores.Add(TScore.Create(s));
    end;
    CloseFile(F);
  end;
end;

procedure THighScores.ShowInListView(listview:TListView);
var
  i: integer;
  score: TScore;
begin
  listview.Items.Clear;
  for i := 0 to FScores.Count-1 do
    with listview.Items.Add do
    begin
      score:= TScore(FScores.Items[i]);
      Caption := IntToStr(i+1);
      SubItems.Add(score.Name);
      SubItems.Add(VALUTUM + ' ' + IntToStr(score.Cash));
      SubItems.Add(IntToStr(score.Health) + '%');
      Data := FScores.Items[i];
    end;
end;

end.
