object frmBank: TfrmBank
  Left = 741
  Top = 463
  Width = 336
  Height = 133
  Caption = 'bank'
  Color = clBtnFace
  Font.Charset = ANSI_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object Label1: TLabel
    Left = 8
    Top = 8
    Width = 63
    Height = 23
    Caption = 'Label1'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -19
    Font.Name = 'Tahoma'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object Label2: TLabel
    Left = 8
    Top = 32
    Width = 31
    Height = 13
    Caption = 'Label2'
  end
  object btnWithdraw: TButton
    Left = 8
    Top = 72
    Width = 75
    Height = 25
    Caption = '&Withdraw'
    TabOrder = 0
    OnClick = btnWithdrawClick
  end
  object btnDeposit: TButton
    Left = 88
    Top = 72
    Width = 75
    Height = 25
    Caption = '&Deposit'
    TabOrder = 1
    OnClick = btnDepositClick
  end
  object Button1: TButton
    Left = 168
    Top = 72
    Width = 75
    Height = 25
    Caption = 'Button1'
    TabOrder = 2
  end
  object btnClose: TButton
    Left = 248
    Top = 72
    Width = 75
    Height = 25
    Caption = '&Close'
    TabOrder = 3
    OnClick = btnCloseClick
  end
end
