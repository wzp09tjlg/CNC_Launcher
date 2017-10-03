import sys,os
# coding=utf-8

def changeVision(svnvision) :
    print "begin changeVision..."
    targetcfg = open(os.getcwd() + "/app/src/main/assets/app.cfg", "w")
    for line in open(os.getcwd() + "/app/src/main/assets/app.cfg_tmp"):
        if line.startswith("APK_SVNREVISION"):
            line = "APK_SVNREVISION = " + svnvision + "\n"
        print line,
        targetcfg.write(line)
    targetcfg.close()
    print "end changeVision... \n"


if __name__=='__main__':
    rootdir = os.getcwd()
    print "rootdir = %s" %rootdir
    conf = rootdir + "/app/src/main/assets/app.cfg"
    tmpconf = conf + "_tmp"
    print conf
    print tmpconf
    if os.path.exists(tmpconf):
        os.remove(tmpconf)
    os.rename(conf, tmpconf)

    svninfo_output = os.popen("svn info")
    updateinfo = svninfo_output.read()
    print updateinfo
    svnvision = "0"
    for str in updateinfo.split('\n'):
        if str.startswith("Revision:"):
            svnvision = str.split(':')[1].strip()
            print svnvision
            break
    changeVision(svnvision)
    print "begin gradlw build ...\n"
    buildout = os.popen("gradlew build")
    print buildout.read()

    buildpath = os.getcwd() + "\\app\\build\\outputs\\apk\\app-channel-release.apk"
    apkpath = os.getcwd()+"\\cnc-release-r"+svnvision+".apk"
    cmd = "copy " + buildpath + " " + apkpath
    print "cmd = %s" %cmd 
    os.popen(cmd)

    
