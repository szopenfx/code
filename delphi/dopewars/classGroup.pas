unit classGroup;

interface

uses
  Contnrs,
  classPerson;

type
  TGroup = class(TObject)
  private
    FGroup: TObjectList;
    function GetPerson(i:integer): TPerson;
  public
    property Group[i:integer]: TPerson read GetPerson; default;
  end;

implementation

function TGroup.GetPerson(i:integer): TPerson;
begin
  if (i > 0) and (i < FGroup.Count) then
    result := TPerson(FGroup.Items[i])
  else
    result := nil;
end;

end.
 