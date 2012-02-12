unit classStack;

interface

type
  TStack = class(TObject)
  private
    stack: array of integer;
    size: integer;
  public
    constructor Create;
    destructor Destroy; override;
    procedure Push(i:integer);
    function Pop: integer;
    function Count: integer;
    procedure Clear;
  end;

implementation

constructor TStack.Create;
begin
  inherited;
  SetLength(stack,0);
  size := 0;
end;

destructor TStack.Destroy;
begin
  SetLength(stack,0);
  inherited;
end;

procedure TStack.Push(i:integer);
begin
  Inc(size);
  SetLength(stack,size);
  stack[size-1] := i;
end;

function TStack.Pop: integer;
begin
  if size > 0 then
  begin
    Dec(size);
    result := stack[size];
    SetLength(stack,size);
  end
  else
    result := 0;
end;

function TStack.Count: integer;
begin
  result := size;
end;

procedure TStack.Clear;
begin
  size := 0;
  SetLength(stack,0);
end;

end.
