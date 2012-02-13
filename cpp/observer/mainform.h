
#ifndef DOTS_H
#define DOTS_H

#include <qobject.h>
#include <qapplication.h>
#include <qvbox.h>
#include <qhbox.h>
#include <qpushbutton.h>
#include <qcolordialog.h>

#include "dotlistbox.h"
#include "dotwidget.h"
#include "points.h"
#include "observer.h"

class MainForm : public QObject, public Observer
{
  Q_OBJECT
  private:
    Points points;
    QApplication app;
    QHBox hbox;
    QVBox vbox;
    DotListBox listbox;
    QPushButton remove;
    QPushButton color;
    QPushButton quit;
    DotWidget dots;
  private slots:
    void currentChanged(QListBoxItem *);
    void removeClicked();
    void colorClicked();
  public:
    MainForm(int argc, char *argv[]);
    virtual ~MainForm();
    int Execute();
    void Update();
};

#endif
