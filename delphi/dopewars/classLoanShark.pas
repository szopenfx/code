unit classLoanShark;

interface

uses
  classPerson;

type
  TLoanShark = class(TPerson)
  public
    constructor Create;
//    procedure BorrowMoney(amount:integer);
  end;

implementation

uses
  classBankAccount;

constructor TLoanShark.Create;
var
  account: TBankAccount;
begin
  account := TBankAccount.Create(Random(20000),3,-2000);
  inherited Create('Adolph W. Bush',Random(5000),account);
end;

end.
 