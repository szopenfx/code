unit classExpression;

interface

type
  TExpression = class
  public
    procedure Print(indent: string); virtual; abstract;
  end;

type
  TExprArray = array of TExpression;

implementation

end.
