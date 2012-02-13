
#include "subject.h"

Subject::Subject()
{
}

Subject::~Subject()
{
}

void Subject::Attach(Observer &o)
{
  observers.push_back(&o);
}

void Subject::Detach(Observer &o)
{
  std::vector<Observer *>::iterator observer;
  for(observer = observers.begin(); observer != observers.end(); observer++)
    if(*observer == &o)
    {
      observers.erase(observer);
      break;
    }
}

void Subject::Notify()
{
  std::vector<Observer *>::iterator observer;
  for(observer = observers.begin(); observer != observers.end(); observer++)
    (*observer)->Update();
}
