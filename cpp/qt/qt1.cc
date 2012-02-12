#include <qapplication.h>
#include <qlabel.h>

int main(int argc, char *argv[])
{
  QApplication app(argc, argv);
  QLabel *hello = new QLabel("Hello, world!", 0);
  
  app.setMainWidget(hello);
  hello->show();
  return app.exec();
}
