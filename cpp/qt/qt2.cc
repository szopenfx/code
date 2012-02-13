#include <qapplication.h>
#include <qpushbutton.h>
#include <qfont.h>

int main(int argc, char *argv[])
{
  QApplication app(argc, argv);
  QPushButton quit("Quit", 0);

  quit.setFont(QFont("Times", 18, QFont::Bold));

  QObject::connect(&quit, SIGNAL(clicked()), &app, SLOT(quit()));
  
  app.setMainWidget(&quit);
  quit.show();
  return app.exec();
}
