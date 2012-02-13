unit classReplicator;
// DrugList that contains one instance of every drug in the game

interface

uses
  StdCtrls, SysUtils,
  classDrugList, classDrug;

type
  TReplicator = class(TDrugList)
  public
    constructor Create;
    destructor Destroy; override;
    procedure ShowInMemo(m:TMemo);
    function SaveToFile: boolean; // true if saving went ok
    function ReadFromFile: boolean; // true if loading went ok
    function Duplicate(name:string; amount:integer): TDrug;
  end;

implementation

uses
  Classes, IniFiles, Forms;

constructor TReplicator.Create;
begin
  inherited Create;
  if not ReadFromFile then
  begin
    FDrux.Add(TDrug.Create('Weed',10));
    FDrux.Add(TDrug.Create('Hash',10));
    FDrux.Add(TDrug.Create('Cocaine',90));
    FDrux.Add(TDrug.Create('Heroin',25));
    FDrux.Add(TDrug.Create('Shroomies',25));
    FDrux.Add(TDrug.Create('Peyote',40));
    FDrux.Add(TDrug.Create('Speed',25));
    FDrux.Add(TDrug.Create('LSD-25',15));
    FDrux.Add(TDrug.Create('Ludes',10));
    FDrux.Add(TDrug.Create('Methadon',10));
    FDrux.Add(TDrug.Create('Morphine',20));
    FDrux.Add(TDrug.Create('XTC',15));
    FDrux.Add(TDrug.Create('Crack',25));
    FDrux.Add(TDrug.Create('DMT',45));
    FDrux.Add(TDrug.Create('Mescaline',22));
    FDrux.Add(TDrug.Create('GHB',25));
    FDrux.Add(TDrug.Create('PCP',30));
    FDrux.Add(TDrug.Create('Salvia',15));
    FDrux.Add(TDrug.Create('MDMA',25));
    FDrux.Add(TDrug.Create('Valium',15));
    FDrux.Add(TDrug.Create('Ketamine',25));
    FDrux.Add(TDrug.Create('Opium',15));
    FDrux.Add(TDrug.Create('DOM/STP',25));
    FDrux.Add(TDrug.Create('N20',25));
    FDrux.Add(TDrug.Create('Ayahuasca',25));
    SaveToFile;
  end;
  Sort(stName);
end;

destructor TReplicator.Destroy;
begin
  SaveToFile;
  inherited Destroy;
end;

function TReplicator.Duplicate(name:string; amount:integer): TDrug;
var
  ADrug: TDrug;
begin
  ADrug := FindDrug(name);
  if ADrug <> nil then
  begin
    result := TDrug.Create(ADrug);
    result.Amount := amount;
  end
  else
    result := nil;
end;

procedure TReplicator.ShowInMemo(m:TMemo);
var
  x: integer;
  d: TDrug;
begin
  m.Lines.Clear;
  for x := 0 to FDrux.Count-1 do
  begin
    d := TDrug(FDrux.Items[x]);
    m.Lines.Add(d.Name + ': fl.' + IntToStr(d.Price));
  end;
end;

function TReplicator.SaveToFile: boolean;
var
  ini: TIniFile;
  druglist: string;
  d: TDrug;
  i: integer;
begin
  ini := TIniFile.Create(ExtractFilePath(Application.ExeName)+'dope.ini');
  druglist := '';
  try
    for i := 0 to FDrux.Count - 1 do
      druglist := druglist + TDrug(FDrux.Items[i]).Name + ';';
    ini.WriteString('Drugs','_DrugList',druglist);
    for i := 0 to FDrux.Count - 1 do
    begin
      d := TDrug(FDrux.Items[i]);
      ini.WriteInteger('Drugs',d.Name,d.Price);
    end;
    result := True;
  finally
    ini.Free;
  end;
end;

function TReplicator.ReadFromFile: boolean;
            function GetNextDrug(var druglist:string): string;
            var X: integer;
            begin
              X := Pos(';',druglist);
              result := Copy(druglist,1,X-1);
              druglist := Copy(druglist,X+1,Length(druglist)-X);
            end;
var
  ini: TIniFile;
  druglist: string;
  drugname: string;
  drugprice: integer;
begin
  ini := TIniFile.Create(ExtractFilePath(Application.ExeName)+'dope.ini');
  try
    druglist := ini.ReadString('Drugs','_DrugList','');
    result := druglist <> '';
    while druglist <> '' do
    begin
      drugname := GetNextDrug(druglist);
      drugprice := ini.ReadInteger('Drugs',drugname,0);
      FDrux.Add(TDrug.Create(drugname,drugprice));
      result := True;
    end;
  finally
    ini.Free;
  end;
end;


end.
