unit classFunctionRegistry;

interface

uses
  classMolecule, classIdentifier;

type
  TStringArray = array of string;
  TMoleculeArray = array of TMolecule;

type
  TFunctionRegistry = class
  private
    FNames: TStringArray;
    FMolecule: TMoleculeArray;
  public
    constructor Create;
    destructor Destroy; override;
    procedure RegisterFunction(name: string; params: TMolecule);
    function GetParamNum(func: string; param: string): Integer;
    function GetParamCount(func: string): Integer;
  end;

implementation

{ TFunctionRegistry }

{
  Constructor
}
constructor TFunctionRegistry.Create;
begin
  inherited Create;
end;

{
  Destructor
}
destructor TFunctionRegistry.Destroy;
begin
  SetLength(FNames, 0);
  SetLength(FMolecule, 0);
  inherited;
end;

{
  Return number of parameters for a function
    func - name of function
}
function TFunctionRegistry.GetParamCount(func: string): Integer;
var
  i: Integer;
begin
  Result := 0;
  for i := 0 to High(FNames) do
    if FNames[i] = func then
      Result := Length(FMolecule[i].Exprs) + 1;
end;

{
  Return index of parameter (0-based)
    func - name of function
    param - name of parameter
}
function TFunctionRegistry.GetParamNum(func, param: string): Integer;
var
  i, j: Integer;
begin
  Result := -1;
  { first find right index according to name of function }
  for i := 0 to High(FNames) do
    if FNames[i] = func then
    begin
      { if the first atom is the parameter, return 0 }
      if TIdentifier(FMolecule[i].Atom).Value = param then
      begin
        Result := 0;
        Break;
      end
      else
      begin
        { if one of the other atoms is the parameter, return > 0 }
        for j := 0 to High(FMolecule[i].Exprs) do
          if TIdentifier(FMolecule[i].Exprs[j]).Value = param then
          begin
            Result := j + 1;
            Break;
          end;
        if Result > -1 then
          Break;
      end;
  end;
end;

{
  Add a function to the registry
    name - name of function
    params - molecule with atoms of parameter names
}
procedure TFunctionRegistry.RegisterFunction(name: string; params: TMolecule);
begin
  SetLength(FNames, Length(FNames) + 1);
  SetLength(FMolecule, Length(FMolecule) + 1);
  FNames[High(FNames)] := name;
  FMolecule[High(FMolecule)] := params;
end;

end.
