unit classMessenger;
// class that receives & displays messages in a TMemo or a TListBox

interface

uses
  StdCtrls, SysUtils;

type
  TMessenger = class(TObject)
  private
    FMemo       : TMemo;
    FListBox    : TListBox;
  public
    constructor Create(memo:TMemo); overload;
    constructor Create(listbox:TListBox); overload;
    procedure AddMessage(msg:string);
    procedure PopupMessage(msg:string);
  end;

implementation

uses
  Dialogs,
  globals, classDopeWars;

constructor TMessenger.Create(memo:TMemo);
begin
  inherited Create;
  FListBox := nil;
  FMemo := memo;
  // this hack is so that there is text on the last line of the memo
  FMemo.Lines.Clear;
  FMemo.Lines.Add(sWelcomeMessage);
  FMemo.Text := sWelcomeMessage;
end;

constructor TMessenger.Create(listbox:TListBox);
begin
  inherited Create;
  FMemo := nil;
  FListBox := listbox;
  FListBox.Items.Add(sWelcomeMessage)
end;

procedure TMessenger.AddMessage(msg:string);
begin
  if FListBox <> nil then
    FListBox.Items.Add(Format(sGeneralMessage,[dope.Time,msg]))
  else
    if FMemo <> nil then
      FMemo.Lines.Add(Format(sGeneralMessage,[dope.Time,msg]));
end;

procedure TMessenger.PopupMessage(msg:string);
begin
  ShowMessage(msg);
  AddMessage(msg);
end;

end.
