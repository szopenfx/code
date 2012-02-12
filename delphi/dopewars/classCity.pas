unit classCity;
// class for all the locations in the game

interface

uses
  Contnrs, StdCtrls, classLocation;

type
  TCity = class(TObject)
  private
    FLox: TObjectList;
    FCityGBX: TGroupBox;
    FActionGBX: TGroupBox;
    function ReadFromFile: boolean;
    function SaveToFile: boolean;
    function GetLocation(i:integer): TLocation;
  public
    constructor Create(citygbx,actiongbx:TGroupBox);
    destructor Destroy; override;
    procedure AddLocation(s:string; i:integer);
    procedure ShowInGroupBox;
    procedure SetDisabled(ALocation:TLocation);
    property Locations[i:Integer]: TLocation read GetLocation; default;
  end;

implementation

uses
  IniFiles, SysUtils, Forms,
  formMain, Controls;

constructor TCity.Create(citygbx,actiongbx:TGroupBox);
begin
  inherited Create;
  FLox := TObjectList.Create;
  FCityGBX := citygbx;
  FActionGBX := actiongbx;
  if not ReadFromFile then
  begin
    AddLocation('Vrijheidswijk',2);
    AddLocation('Station',1);
    AddLocation('Prinsentuin',16);
    AddLocation('Achter de Hoven',2+4);
    AddLocation('Wêze',8);
    AddLocation('Zaailand',1);
    AddLocation('Politiebureau',0);
    SaveToFile;
  end;
  ShowInGroupBox;
end;

destructor TCity.Destroy;
begin
  SaveToFile;
  FLox.Free;
end;

procedure TCity.AddLocation(s:string; i:integer);
begin
  FLox.Add(TLocation.Create(s,i));
end;

function TCity.GetLocation(i:integer): TLocation;
begin
  if (i > 0) and (i < FLox.Count) then
    result := TLocation(FLox.Items[i])
  else
    result := nil;
end;

procedure TCity.ShowInGroupBox;
var
  i: integer;
begin
  for i := FCityGBX.ControlCount - 1 downto 0 do
    if FCityGBX.Controls[i].ClassName = 'TButton' then
      FCityGBX.Controls[i].Free;
  FCityGBX.Height := (FLox.Count + 1) * 25;
  for i := 0 to FLox.Count - 1 do
    with TButton.Create(FCityGBX) do
    begin
      Parent := FCityGBX;
      SetBounds(8,16 + i * 25,FCityGBX.Width - 16,25);
      Caption := TLocation(FLox.Items[i]).Name;
      OnClick := Main.LocationButtonClick;
      Tag := Integer(Flox.Items[i]);
    end;
end;

procedure TCity.SetDisabled(ALocation:TLocation);
var
  i,t: Integer;
  e: Boolean;
begin
  for i := 0 to FCityGBX.ControlCount - 1 do
  begin
    t := FCityGBX.Controls[i].Tag;
    e := TLocation(t) <> ALocation;
    TButton(FCityGBX.Controls[i]).Enabled := e;
  end;
  for i := 0 to FActionGBX.ControlCount - 1 do
  begin
    t := TButton(FActionGBX.Controls[i]).Tag;
    e := t and ALocation.Actions = t;
    TButton(FActionGBX.Controls[i]).Enabled := e;
  end;
end;

function TCity.SaveToFile: boolean;
var
  ini: TIniFile;
  placelist: string;
  L: TLocation;
  i: integer;
begin
  ini := TIniFile.Create(ExtractFilePath(Application.ExeName)+'dope.ini');
  placelist := '';
  result := False;
  try
    for i := 0 to Flox.Count-1 do
      placelist := placelist + TLocation(Flox.Items[i]).Name + ';';
    ini.WriteString('Locations','_PlaceList',placelist);
    for i := 0 to Flox.Count-1 do
    begin
      L := TLocation(Flox.Items[i]);
      ini.WriteInteger('Locations',L.Name,L.Actions);
      result := True;
    end;
  finally
    ini.Free;
  end;
end;

function TCity.ReadFromFile: boolean;
          function GetNextLocation(var placelist:string): string;
          var X: integer;
          begin
            X := Pos(';',placelist);
            result := Copy(placelist,1,X-1);
            placelist := Copy(placelist,X+1,Length(placelist)-X);
          end;
var
  ini: TIniFile;
  placelist: string;
  placename: string;
begin
  ini := TIniFile.Create(ExtractFilePath(Application.ExeName)+'dope.ini');
  result := False;
  try
    placelist := ini.ReadString('Locations','_PlaceList','');
    while placelist <> '' do
    begin
      placename := GetNextLocation(placelist);
      FLox.Add(TLocation.Create(placename,ini.ReadInteger('Locations',placename,0)));
      result := True;
    end;
  finally
    ini.Free;
  end;
end;



end.
