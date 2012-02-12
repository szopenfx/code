unit classPeople;

interface

uses
  classPerson, classPlayer, classArmsDealer, classDoctor, classPimp;

type
  TPeople = class(TObject)
  private
    FPlayer: TPlayer;
    FArmsDealer: TArmsDealer;
    FDoctor: TDoctor;
    FPimp: TPimp;
    procedure NewArmsDealer;
    procedure NewDoctor;
    procedure NewPimp;
  public
    constructor Create;
    destructor Destroy; override;
    procedure AddInterest;
    procedure BringOutTheDead;
    property Player: TPlayer read FPlayer;
    property ArmsDealer: TArmsDealer read FArmsDealer;
    property Doctor: TDoctor read FDoctor;
    property Pimp: TPimp read FPimp;
  end;

implementation

uses
  classBankAccount, globals;

constructor TPeople.Create;
begin
  inherited Create;
  FPlayer := TPlayer.Create;
  NewArmsDealer;
  NewDoctor;
  NewPimp;
end;

destructor TPeople.Destroy;
begin
  FPimp.Free;
  FDoctor.Free;
  FArmsDealer.Free;
  FPlayer.Free;
  inherited Destroy;
end;

procedure TPeople.AddInterest;
begin
  FPlayer.BankAccount.AddInterest;
  FArmsDealer.BankAccount.AddInterest;
  FDoctor.BankAccount.AddInterest;
  FPimp.BankAccount.AddInterest;
end;

procedure TPeople.BringOutTheDead;
begin
  FArmsDealer.MedicalUpdate;
  FDoctor.MedicalUpdate;
  FPimp.MedicalUpdate;
  FPlayer.MedicalUpdate;
  if FArmsDealer.IsDead then NewArmsDealer;
  if FDoctor.IsDead then NewDoctor;
  if FPimp.IsDead then NewPimp;
end;

procedure TPeople.NewArmsDealer;
begin
  FArmsDealer.Free;
  FArmsDealer := TArmsDealer.Create;
end;

procedure TPeople.NewDoctor;
begin
  FDoctor.Free;
  FDoctor := TDoctor.Create(10);
end;

procedure TPeople.NewPimp;
begin
  FPimp.Free;
  FPimp := TPimp.Create;
end;

end.
