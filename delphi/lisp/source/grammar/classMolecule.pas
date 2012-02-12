unit classMolecule;

interface

uses
  classExpression, classAtom;

type
  TMolecule = class(TExpression)
  private
    FAtom: TAtom;
    FExprs: TExprArray;
  public
    constructor Create(atom: TAtom; exprs: TExprArray);
    destructor Destroy; override;
    procedure Print(indent: string); override;
    property Atom: TAtom read FAtom;
    property Exprs: TExprArray read FExprs;
  end;

implementation

{ TMolecule }

constructor TMolecule.Create(atom: TAtom; exprs: TExprArray);
begin
  inherited Create;
  FAtom := atom;
  FExprs := exprs;
end;

destructor TMolecule.Destroy;
var
  i: Integer;
begin
  FAtom.Free;
  for i := 0 to High(FExprs) do
    FExprs[i].Free;
  SetLength(FExprs, 0);
  inherited Destroy;
end;

procedure TMolecule.Print(indent: string);
var
  i: Integer;
begin
  Writeln(indent + 'Molecule');
  FAtom.Print(indent + '  ');
  for i := 0 to High(FExprs) do
    FExprs[i].Print(indent + '  ');
end;

end.
