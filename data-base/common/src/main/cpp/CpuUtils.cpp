#include "CpuUtils.h"

namespace CpuUtils {

    int getCpuCoreCount() {
        int cores = 0;
        DIR *dir;
        struct dirent *ent;
        if ((dir = opendir("/sys/devices/system/cpu/")) != nullptr) {
            while ((ent = readdir(dir)) != nullptr) {
                std::string path = ent->d_name;
                if (path.find("cpu") == 0) {
                    bool isCore = true;
                    for (int i = 3; i < path.length(); i++) {
                        if (path[i] < '0' || path[i] > '9') {
                            isCore = false;
                            break;
                        }
                    }
                    if (isCore) {
                        cores++;
                    }
                }
            }
            closedir(dir);
        }
        return cores;
    }

    int getMaxFreqCore() {
        int count = getCpuCoreCount();
        int maxFreq = -1;
        for (int i = 0; i < count; i++) {
            std::string filename = "/sys/devices/system/cpu/cpu" +
                                   std::to_string(i) + "/cpufreq/cpuinfo_max_freq";
            std::ifstream cpuInfoMaxFreqFile(filename);
            if (cpuInfoMaxFreqFile.is_open()) {
                std::string line;
                if (std::getline(cpuInfoMaxFreqFile, line)) {
                    try {
                        int freqBound = std::stoi(line);
                        if (freqBound > maxFreq) maxFreq = freqBound;
                    } catch (const std::invalid_argument &e) {

                    }
                }
                cpuInfoMaxFreqFile.close();
            }
        }
        return maxFreq;
    }

    void bindMainThread() {
        int coreNum = getMaxFreqCore();
        //CPU核的集合
        cpu_set_t mask;
        //将mask置空
        CPU_ZERO(&mask);
        //将需要绑定的CPU核序列设置给mask，核为序列0,1,2,3……
        CPU_SET(coreNum, &mask);
        //将主线程绑核
        if (sched_setaffinity(0, sizeof(mask), &mask) == -1) {
            printf("bind core fail");
        }
    }

}
