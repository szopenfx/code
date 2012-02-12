unit classState;

interface

uses
  declarations;

type
  TCheckType = ( cContains, cPeek, cMagicPeek );
  TLogType = ( lLog, lNoLog );

type
  TState = class
  protected
    FState: Integer;
    FNext: Integer;
    FCheck: TCheckType;
    FAllowed: string;
    FToken: TTokenType;
    FLog: TLogType;
  public
    constructor Create(
      state, next: Integer;
      check: TCheckType;
      allowed: string;
      token: TTokenType;
      log: TLogType
    );
    property State: Integer read FState;
    property Next: Integer read FNext;
    property Check: TCheckType read FCheck;
    property Allowed: string read FAllowed;
    property Token: TTokenType read FToken write FToken;
    property Log: TLogType read FLog;
  end;

implementation

{ TState }

constructor TState.Create(
  state, next: Integer;
  check: TCheckType;
  allowed: string;
  token: TTokenType;
  log: TLogType
);
begin
  inherited Create;
  FState := state;
  FNext := next;
  FCheck := check;
  FAllowed := allowed;
  FToken := token;
  FLog := log;
end;

end.
