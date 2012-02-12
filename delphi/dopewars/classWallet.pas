unit classWallet;
// class for the money a TPerson has

interface

type
  TWallet = class(TObject)
  private
    FCash: integer;
  public
    constructor Create(amount:integer);
    procedure Earn(AWallet:TWallet); overload;
    procedure Earn(amount:integer); overload;
    function Spend(amount:integer): boolean; // true if successful
    property Cash: integer read FCash;
  end;

implementation

constructor TWallet.Create(amount:integer);
begin
  inherited Create;
  FCash := amount;
end;

procedure TWallet.Earn(AWallet:TWallet);
begin
  FCash := FCash + AWallet.Cash;
end;

procedure TWallet.Earn(amount:integer);
begin
  FCash := FCash + amount;
end;

function TWallet.Spend(amount:integer): boolean;
begin
  result := FCash >= amount;
  if result then
    FCash := FCash - amount;
end;

end.
