object frmDoctor: TfrmDoctor
  Left = 326
  Top = 434
  Width = 417
  Height = 182
  Caption = 'doctor'
  Color = clBtnFace
  Font.Charset = ANSI_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object lblHP: TLabel
    Left = 8
    Top = 8
    Width = 60
    Height = 33
    Caption = 'lblHP'
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -27
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentFont = False
  end
  object lblPrice: TLabel
    Left = 8
    Top = 40
    Width = 33
    Height = 13
    Caption = 'lblPrice'
  end
  object lblKidneyBuy: TLabel
    Left = 8
    Top = 56
    Width = 60
    Height = 13
    Caption = 'lblKidneyBuy'
  end
  object lblKidneySell: TLabel
    Left = 8
    Top = 72
    Width = 58
    Height = 13
    Caption = 'lblKidneySell'
  end
  object btnHeal: TButton
    Left = 8
    Top = 120
    Width = 75
    Height = 25
    Caption = '&Fix'
    TabOrder = 0
    OnClick = btnHealClick
  end
  object btnViolence: TButton
    Left = 88
    Top = 120
    Width = 75
    Height = 25
    Caption = '&Violence'
    TabOrder = 1
    OnClick = btnViolenceClick
  end
  object btnClose: TButton
    Left = 328
    Top = 120
    Width = 75
    Height = 25
    Caption = '&Close'
    TabOrder = 2
    OnClick = btnCloseClick
  end
  object btnSellKidneys: TButton
    Left = 248
    Top = 120
    Width = 75
    Height = 25
    Caption = '&Sell Kidney'
    TabOrder = 3
    OnClick = btnSellKidneysClick
  end
  object btnBuyKidneys: TButton
    Left = 168
    Top = 120
    Width = 75
    Height = 25
    Caption = '&Buy Kidney'
    TabOrder = 4
    OnClick = btnBuyKidneysClick
  end
end
