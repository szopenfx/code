unit classBankAccount;
// class for a person's bankroll

interface

type
  TBankAccount = class(TObject)
  private
    FBalance: integer;
    FInterest: integer;
    FDebtLimit: integer;
  public
    constructor Create(balance,interest,debtlimit:integer);
    function Withdraw(amount:integer): boolean;
    procedure Deposit(amount:integer);
    procedure AddInterest;
    property Balance: integer read FBalance;
    property Interest: integer read FInterest;
    property DebtLimit: integer read FDebtLimit;
  end;

implementation

constructor TBankAccount.Create;
begin
  inherited Create;
  FBalance := balance;
  FInterest := interest;
  FDebtLimit := debtlimit;
end;

function TBankAccount.Withdraw;
var
  newBalance: integer;
begin
  newBalance := FBalance - amount;
  result := newBalance >= FDebtLimit;
  if result then
    FBalance := newBalance;
end;

procedure TBankAccount.Deposit;
begin
  FBalance := FBalance + amount;
end;

procedure TBankAccount.AddInterest;
begin
  FBalance := Trunc(FBalance * (1 + (FInterest / 100)));
end;

end.
