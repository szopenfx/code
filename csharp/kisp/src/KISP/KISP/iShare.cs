using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;

namespace KISP.Search
{
    interface iShare
    {
        XmlDocument Find(string SearchString, XmlDocument FillinXmlDocument);
    }
}
