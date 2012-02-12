
#ifndef SUBJECT_H
#define SUBJECT_H

#include <vector>
#include "observer.h"

class Subject
{
  private:
    std::vector<Observer *> observers;
  public:
    Subject();
    ~Subject();
    void Attach(Observer &o);
    void Detach(Observer &o);
    void Notify();
};

#endif
