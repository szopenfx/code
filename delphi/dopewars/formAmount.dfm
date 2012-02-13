object frmAmount: TfrmAmount
  Left = 524
  Top = 379
  BorderIcons = []
  BorderStyle = bsDialog
  Caption = 'frmAmount'
  ClientHeight = 124
  ClientWidth = 293
  Color = clBtnFace
  Font.Charset = ANSI_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  DesignSize = (
    293
    124)
  PixelsPerInch = 96
  TextHeight = 13
  object lblCanBuy: TLabel
    Left = 8
    Top = 8
    Width = 47
    Height = 13
    Caption = 'lblCanBuy'
  end
  object lblWarning: TLabel
    Left = 8
    Top = 104
    Width = 60
    Height = 13
    Caption = 'lblWarning'
    Font.Charset = ANSI_CHARSET
    Font.Color = clRed
    Font.Height = -11
    Font.Name = 'Tahoma'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object lblAmount: TLabel
    Left = 8
    Top = 24
    Width = 47
    Height = 13
    Caption = 'lblAmount'
  end
  object btnOK: TButton
    Left = 224
    Top = 72
    Width = 65
    Height = 25
    Anchors = [akTop, akRight]
    Caption = 'OK'
    Default = True
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 0
    OnClick = btnOKClick
  end
  object btnCancel: TButton
    Left = 224
    Top = 40
    Width = 65
    Height = 25
    Anchors = [akTop, akRight]
    Cancel = True
    Caption = 'Cancel'
    TabOrder = 1
    OnClick = btnCancelClick
  end
  object edtAmount: TEdit
    Left = 8
    Top = 40
    Width = 209
    Height = 21
    TabOrder = 2
    Text = 'edtAmount'
    OnChange = edtAmountChange
  end
  object btnMinOne: TButton
    Tag = 1
    Left = 80
    Top = 72
    Width = 33
    Height = 25
    Caption = '-1'
    TabOrder = 3
    OnClick = btnDeltaClick
  end
  object btnPlsOne: TButton
    Tag = 2
    Left = 112
    Top = 72
    Width = 33
    Height = 25
    Caption = '+1'
    TabOrder = 4
    OnClick = btnDeltaClick
  end
  object btnMinTen: TButton
    Tag = 3
    Left = 152
    Top = 72
    Width = 33
    Height = 25
    Caption = '-10'
    TabOrder = 5
    OnClick = btnDeltaClick
  end
  object btnPlsTen: TButton
    Tag = 4
    Left = 184
    Top = 72
    Width = 33
    Height = 25
    Caption = '+10'
    TabOrder = 6
    OnClick = btnDeltaClick
  end
  object btnMin: TButton
    Left = 8
    Top = 72
    Width = 33
    Height = 25
    Caption = '1'
    TabOrder = 7
    OnClick = btnMinMaxClick
  end
  object btnMax: TButton
    Left = 40
    Top = 72
    Width = 33
    Height = 25
    Caption = 'Max'
    TabOrder = 8
    OnClick = btnMinMaxClick
  end
end
