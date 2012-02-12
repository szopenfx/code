using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;

namespace KISP.Nexus
{
    /// <summary>
    /// This class wraps around an XML document and provides some useful methods for accessing the
    /// XML data - see Configuration.cs for example usage
    /// </summary>
    class XMLProxy
    {
        private string _Filename;
        private XmlDocument _XML = null;

        /// <summary>
        /// Create XML Proxy object
        /// </summary>
        /// <param name="Filename"></param>
        public XMLProxy(string Filename)
        {
            _Filename = Filename;
            _XML = new XmlDocument();
            _XML.Load(_Filename);
        }

        /// <summary>
        /// Returns the value of the first child element of an element specified by an XPath expression
        /// </summary>
        /// <param name="XPath">An XPath expression</param>
        /// <returns>String value of node</returns>
        public string XPath(string XPath)
        {
            XmlNode Node = _XML.SelectSingleNode(XPath);
            if (Node == null)
                throw new Exception("Configuration couldn't find XPath expression '" + XPath + "'");
            XmlNode FirstChild = Node.FirstChild;
            return FirstChild.Value;
        }

        /// <summary>
        /// Gets an XPath expression of the form /settings/user/name
        /// </summary>
        /// <param name="Section">The section, user in the example</param>
        /// <param name="Item">The item, name in the example</param>
        /// <returns>The value of the first child of the XPath expression</returns>
        public string XPath(string Section, string Item)
        {
            string x = string.Format("/settings/{0}/{1}", Section, Item);
            return XPath(x);
        }

        /// <summary>
        /// Gets an XPath expression like /settings/section/listitem[2]/item
        /// </summary>
        /// <param name="Section">Section of list item</param>
        /// <param name="ListItem">Name of list item</param>
        /// <param name="Index">0-based index of list item</param>
        /// <param name="Item"></param>
        /// <returns>The value of the first child of the XPath expression</returns>
        public string XPath(string Section, string ListItem, int Index, string Item)
        {
            string x = string.Format(
                "/settings/{0}/{1}[{2}]/{3}",
                Section, ListItem, Index + 1, Item
            );
            return XPath(x);
        }

        /// <summary>
        /// Get string from XML of form /settings/section/item
        /// </summary>
        /// <param name="Section">section</param>
        /// <param name="Item">item</param>
        /// <returns>string value of firstchild</returns>
        public string GetStr(string Section, string Item)
        {
            return XPath(Section, Item);
        }

        /// <summary>
        /// Get int of form /settings/user/age
        /// </summary>
        /// <param name="Section">section, user in example</param>
        /// <param name="Item">item, age in example</param>
        /// <returns>value of firstchild</returns>
        public int GetInt(string Section, string Item)
        {
            return Int32.Parse(XPath(Section, Item));
        }

        /// <summary>
        /// Get count of all nodes of form /settings/shares/share
        /// </summary>
        /// <param name="Section">section, shares in example</param>
        /// <param name="ListItem">item, share in example</param>
        /// <returns>node count of xpath expression</returns>
        public int ListCount(string Section, String ListItem)
        {
            string x = string.Format("/settings/{0}/{1}", Section, ListItem);
            return _XML.SelectNodes(x).Count;
        }

        /// <summary>
        /// Get string of form /settings/shares/share[2]/type
        /// </summary>
        /// <param name="Section">section, shares in example</param>
        /// <param name="ListItem">list item, share in example</param>
        /// <param name="Index">index, 2 in example (needs to be given as 0-based index!!)</param>
        /// <param name="Item">item, type in example</param>
        /// <returns>the value of the firstchild</returns>
        public string GetListStr(string Section, string ListItem, int Index, string Item)
        {
            return XPath(Section, ListItem, Index, Item);
        }

        /// <summary>
        /// As GetListStr, but returns int
        /// </summary>
        public int GetListInt(string Section, string ListItem, int Index, string Item)
        {
            return Int32.Parse(XPath(Section, ListItem, Index, Item));
        }
    }
}
