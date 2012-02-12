unit formAmount;
// dialog to get an amount of drugs, or money ;)

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, classDrug, StdCtrls, Spin, globals;

type
  TfrmAmount = class(TForm)
    lblCanBuy: TLabel;
    btnOK: TButton;
    btnCancel: TButton;
    edtAmount: TEdit;
    btnMinOne: TButton;
    btnPlsOne: TButton;
    btnMinTen: TButton;
    btnPlsTen: TButton;
    lblWarning: TLabel;
    lblAmount: TLabel;
    btnMin: TButton;
    btnMax: TButton;
    procedure btnOKClick(Sender: TObject);
    procedure btnCancelClick(Sender: TObject);
    procedure edtAmountChange(Sender: TObject);
    procedure btnDeltaClick(Sender: TObject);
    procedure btnMinMaxClick(Sender: TObject);
  private
    FMin: integer;
    FMax: integer;
    FPerUnit: integer;
    FDlgRc: dlgrc;
    procedure SetNiceWidth;
    function IsNumeric(s:string): boolean;
  public
    function ShowForm(dlg:dlgrc; drugname:string; max,perunit:integer): integer;
  end;

var
  frmAmount: TfrmAmount;

implementation

{$R *.dfm}

procedure TfrmAmount.btnOKClick(Sender: TObject);
begin
  ModalResult := StrToInt(edtAmount.Text);
end;

procedure TfrmAmount.btnCancelClick(Sender: TObject);
begin
  ModalResult := -1;
end;

procedure TfrmAmount.SetNiceWidth;
begin
  if lblCanBuy.Width > edtAmount.Width
    then Width := lblCanBuy.Width + btnOK.Width + 50
    else Width := edtAmount.Width + btnOK.Width + 50;
end;

function TfrmAmount.ShowForm(dlg:dlgrc; drugname:string; max,perunit:integer): integer;
begin
  FDlgRc := dlg;
  FMin := 0;
  FMax := max;
  FPerUnit := perunit;
  btnMax.Caption := IntToStr(max);
  edtAmount.Text := IntToStr(FMax);
  Caption := FDlgRc.cap;
  if Pos('%s',FDlgRc.msg) > 0 // o_o
    then lblCanBuy.Caption := Format(FDlgRc.msg,[FMax,drugname])
    else lblCanBuy.Caption := Format(FDlgRc.msg,[FMax]);
  if FDlgRc.amt = ''
    then lblAmount.Caption := ''
    else lblAmount.Caption := Format(FDlgRc.amt,[FMax,FMax * FPerUnit]);

  SetNiceWidth;
  result := ShowModal;
end;

procedure TfrmAmount.edtAmountChange(Sender: TObject);
var
  i: integer;
begin
  if IsNumeric(edtAmount.Text) then
  begin
    i := StrToInt(edtAmount.Text);
    if FDlgRc.amt <> '' then
      lblAmount.Caption := Format(FDlgRc.amt,[i,i * FPerUnit]);
    if i > FMax
      then lblWarning.Caption := 'Value too high'
      else lblWarning.Caption := '';
    btnOK.Enabled := (i <= FMax) and (i <> 0);
    btnMinOne.Enabled := (i - 1 >= FMin);
    btnPlsOne.Enabled := (i + 1 <= FMax);
    btnMinTen.Enabled := (i - 10 >= FMin);
    btnPlsTen.Enabled := (i + 10 <= FMax);
  end
  else
  begin
    btnOK.Enabled := False;
    btnMinOne.Enabled := False;
    btnPlsOne.Enabled := False;
    btnMinTen.Enabled := False;
    btnPlsTen.Enabled := False;
  end;
end;

procedure TfrmAmount.btnDeltaClick(Sender: TObject);
const
  a: array[1..4] of integer = ( -1, +1, -10, +10 );
var
  i: integer;
begin
  if IsNumeric(edtAmount.Text) then
    edtAmount.Text := IntToStr(StrToInt(edtAmount.Text) +
      a[TButton(Sender).Tag]);
end;

function TfrmAmount.IsNumeric(s:string): boolean;
var i: integer;
begin
  result := s <> '';
  if result then
    for i := 1 to Length(s) do
      if s[i] in ['0'..'9'] then
        lblWarning.Caption := ''
      else
      begin
        result := False;
        lblWarning.Caption := 'Illegal character(s)';
      end;
end;

procedure TfrmAmount.btnMinMaxClick(Sender: TObject);
begin
  edtAmount.Text := TButton(Sender).Caption;
end;

end.
