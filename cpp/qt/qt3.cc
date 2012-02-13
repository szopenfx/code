#include <qapplication.h>
#include <qpushbutton.h>
#include <qfont.h>
#include <qvbox.h>

int main(int argc, char *argv[])
{
  QApplication app(argc, argv);
  QVBox vbox;
  QPushButton quit("Quit", &vbox);

  QObject::connect(&quit, SIGNAL(clicked()), &app, SLOT(quit()));

  vbox.resize(200, 120);
  quit.setFont(QFont("Times", 18, QFont::Bold));
  app.setMainWidget(&vbox);
  vbox.show();
  return app.exec();  
}
