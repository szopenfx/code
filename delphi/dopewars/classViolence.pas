unit classViolence;

interface

uses
  classPerson, Contnrs, Forms;

type
  TViolence = class
  private
    FGroup: TObjectList;
    function GetGroupPerson(i:integer): TPerson;
    function GetGroupCount: integer;
  public
    constructor Create;
    destructor Destroy; override;
    procedure AddPerson(APerson:TPerson);
    procedure Fight(weaponindex:integer);
    procedure Reset;
    property Group[i:integer]: TPerson read GetGroupPerson;
    property GroupCount: integer read GetGroupCount;
  end;

implementation

uses
  classDopeWars;

constructor TViolence.Create;
begin
  inherited Create;
  FGroup := TObjectList.Create(False);
end;

destructor TViolence.Destroy;
begin
  FGroup.Free;
  inherited Destroy;
end;

function TViolence.GetGroupPerson(i:integer): TPerson;
begin
  if (i >= 0) and (i < FGroup.Count) then
    result := TPerson(FGroup.Items[i]);
end;

function TViolence.GetGroupCount: integer;
begin
  result := FGroup.Count;
end;

procedure TViolence.AddPerson(APerson:TPerson);
begin
  FGroup.Add(APerson);
end;

procedure TViolence.Fight(weaponindex:integer);
begin
  dope.People.Player.Holster[weaponindex].Shoot(Group[0]);
  if Group[0].Holster.Count > 0 then
    Group[0].Holster[1].Shoot(dope.People.Player);
//  if Group[0].IsDead then
//    FGroup.Delete(0);
end;

procedure TViolence.Reset;
begin
  FGroup.Clear;
end;


end.

