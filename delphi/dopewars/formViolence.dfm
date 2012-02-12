object frmViolence: TfrmViolence
  Left = 427
  Top = 257
  Width = 418
  Height = 256
  Caption = 'YEAH BABY!'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object gbPlayer: TGroupBox
    Left = 8
    Top = 16
    Width = 185
    Height = 105
    Caption = 'gbPlayer'
    Ctl3D = False
    ParentCtl3D = False
    TabOrder = 0
    object gPlayerHP: TGauge
      Left = 80
      Top = 72
      Width = 100
      Height = 28
      Progress = 59
    end
    object Label1: TLabel
      Left = 8
      Top = 16
      Width = 32
      Height = 13
      Caption = 'Label1'
    end
  end
  object gbGroup: TGroupBox
    Left = 216
    Top = 16
    Width = 185
    Height = 105
    Caption = 'gbGroup'
    Ctl3D = False
    ParentCtl3D = False
    TabOrder = 1
    object gGroupHP: TGauge
      Left = 80
      Top = 72
      Width = 100
      Height = 28
      Progress = 59
    end
  end
  object cmbWeapons: TComboBox
    Left = 8
    Top = 128
    Width = 161
    Height = 21
    ItemHeight = 13
    TabOrder = 2
    Text = '<Select weapon>'
    OnChange = cmbWeaponsChange
  end
  object btnAttack: TButton
    Left = 176
    Top = 128
    Width = 75
    Height = 25
    Caption = '&Attack'
    TabOrder = 3
    OnClick = btnAttackClick
  end
  object btnMoney: TButton
    Left = 176
    Top = 160
    Width = 75
    Height = 25
    Caption = 'Take &money'
    TabOrder = 4
    OnClick = btnMoneyClick
  end
  object btnWeapons: TButton
    Left = 8
    Top = 160
    Width = 81
    Height = 25
    Caption = 'Take &weapons'
    TabOrder = 5
    OnClick = btnWeaponsClick
  end
  object btnDrugs: TButton
    Left = 96
    Top = 160
    Width = 75
    Height = 25
    Caption = 'Take &drugs'
    TabOrder = 6
    OnClick = btnDrugsClick
  end
end
