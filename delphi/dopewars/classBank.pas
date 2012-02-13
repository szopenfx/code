unit classBank;

interface

uses
  Contnrs,
  classPerson, classBankAccount;

type
  TBank = class(TPerson)
  private
    FAccounts: TObjectList;
    function GetAccount(i:integer): TBankAccount;
    function FindIndex(account:TBankAccount): integer;
  public
    constructor Create;
    destructor Destroy; override;
    procedure RegisterAccount(bankaccount:TBankAccount);
//    function Withdraw(account:TBankAccount; amount:integer): boolean;
  //  procedure Deposit(account:TBankAccount; amount:integer);
  end;

implementation

constructor TBank.Create;
begin
  inherited Create('John D. Rockefeller',10000,nil);
  FAccounts := TObjectList.Create(False);
end;

destructor TBank.Destroy;
begin
  FAccounts.Free;
  inherited Destroy;
end;

function TBank.GetAccount(i:integer): TBankAccount;
begin
  if (i >= 0) and (i < FAccounts.Count) then
    result := TBankAccount(FAccounts.Items[i])
  else
    result := nil;
end;

procedure TBank.RegisterAccount(bankaccount:TBankAccount);
begin
  FAccounts.Add(bankaccount);
end;

function TBank.FindIndex(account:TBankAccount): integer;
var
  i: integer;
begin
  i := FAccounts.Count;
  repeat
    Dec(i)
  until (account = FAccounts[i]) or (i < 0);
end;

{function TBank.Withdraw(account:TBankAccount; amount:integer): boolean;
begin
  acc}

end.

