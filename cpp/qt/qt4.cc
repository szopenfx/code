#include <qapplication.h>
#include <qpushbutton.h>
#include <qfont.h>

class MyWidget : public QWidget
{
  public:
    MyWidget(QWidget *parent = 0, const char *name = 0);
};

MyWidget::MyWidget(QWidget *parent, const char *name)
  : QWidget(parent, name)
{
  setMinimumSize(200, 120);
  setMaximumSize(200, 120);
  
  QPushButton *quit = new QPushButton("Quit", this, "quit");
  quit->setGeometry(62, 40, 75, 30);
  quit->setFont(QFont("Times", 18, QFont::Bold));
  connect(quit, SIGNAL(clicked()), qApp, SLOT(quit()));
}

int main(int argc, char *argv[])
{
  QApplication app(argc, argv);
  MyWidget mw;
  mw.setGeometry(100, 100, 200, 120);
  app.setMainWidget(&mw);
  mw.show();
  return app.exec();
}

