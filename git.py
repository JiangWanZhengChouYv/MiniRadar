# -*- coding: utf-8 -*-
import subprocess
import sys

def fast_commit():
    # 获取提交信息，无输入则默认
    msg = sys.argv[1] if len(sys.argv) > 1 else "mini-radar 快速提交"
    
    try:
        # Git 三连击
        subprocess.run(["git", "add", "."], check=True)
        subprocess.run(["git", "commit", "-m", msg], check=True)
        subprocess.run(["git", "push"], check=True)
        
        print("\n✅ 提交推送成功！速度拉满！")
    except Exception as e:
        print(f"\n❌ 提交失败：{str(e)}")

if __name__ == "__main__":
    fast_commit()