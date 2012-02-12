object frmBrothel: TfrmBrothel
  Left = 654
  Top = 333
  Width = 340
  Height = 173
  Caption = 'brothel'
  Color = clBtnFace
  Font.Charset = ANSI_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object lblPimp: TLabel
    Left = 8
    Top = 8
    Width = 32
    Height = 13
    Caption = 'lblPimp'
  end
  object lblBitchBuy: TLabel
    Left = 8
    Top = 32
    Width = 51
    Height = 13
    Caption = 'lblBitchBuy'
  end
  object lblBitchSell: TLabel
    Left = 8
    Top = 56
    Width = 49
    Height = 13
    Caption = 'lblBitchSell'
  end
  object lblHave: TLabel
    Left = 8
    Top = 80
    Width = 35
    Height = 13
    Caption = 'lblHave'
  end
  object btnBuyBitch: TButton
    Left = 8
    Top = 112
    Width = 75
    Height = 25
    Caption = '&Buy bitch'
    TabOrder = 0
    OnClick = btnBuyBitchClick
  end
  object btnSellBitch: TButton
    Left = 88
    Top = 112
    Width = 75
    Height = 25
    Caption = '&Sell bitch'
    TabOrder = 1
    OnClick = btnSellBitchClick
  end
  object btnViolence: TButton
    Left = 168
    Top = 112
    Width = 75
    Height = 25
    Caption = '&Violence'
    TabOrder = 2
    OnClick = btnViolenceClick
  end
  object btnClose: TButton
    Left = 248
    Top = 112
    Width = 75
    Height = 25
    Caption = '&Close'
    TabOrder = 3
    OnClick = btnCloseClick
  end
end
