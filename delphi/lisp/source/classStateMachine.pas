unit classStateMachine;

interface

uses
  declarations, classState, SysUtils;

type
  TStateArray = array of TState;

type
  TStateMachine = class
  protected
    FStates: TStateArray;
    FCurrent: Integer;
    FCurrentIndex: Integer;
    function GetCurrentState: TState;
  public
    constructor Create;
    destructor Destroy; override;
    procedure Add(state: TState);
    procedure FindNext;
    procedure SetNext;
    property State: TState read GetCurrentState;
  end;

implementation

{ TStateMachine }

constructor TStateMachine.Create;
begin
  inherited Create;
  FCurrent := 0;
  FCurrentIndex := 0;
end;

destructor TStateMachine.Destroy;
var
  i: Integer;
begin
  for i := 0 to High(FStates) do
    FStates[i].Free;
  SetLength(FStates, 0);
  inherited Destroy;
end;

procedure TStateMachine.Add(state: TState);
begin
  SetLength(FStates, Length(FStates) + 1);
  FStates[High(FStates)] := state;
end;

procedure TStateMachine.FindNext;
begin
  if (FCurrentIndex < (Length(FStates) - 1))
    and (FStates[FCurrentIndex + 1].State = FStates[FCurrentIndex].State)
  then Inc(FCurrentIndex)
  else raise Exception.Create('Scanner fubar');
end;

procedure TStateMachine.SetNext;
var
  next: Integer;
begin
  next := FStates[FCurrentIndex].Next;
  FCurrentIndex := -1;
  repeat
    Inc(FCurrentIndex);
  until FStates[FCurrentIndex].State = next;
end;

function TStateMachine.GetCurrentState: TState;
begin
  Result := FStates[FCurrentIndex];
end;

end.
