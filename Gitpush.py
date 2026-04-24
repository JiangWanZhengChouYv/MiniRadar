import subprocess
import sys

def git_auto_commit():
    # 1. 获取提交信息（如果没输入，默认用快速提交）
    commit_msg = sys.argv[1] if len(sys.argv) > 1 else "快速提交"

    try:
        # 2. 一键三连：添加所有文件 + 提交 + 推送到远程
        subprocess.run(["git", "add", "."], check=True, capture_output=True, text=True)
        subprocess.run(["git", "commit", "-m", commit_msg], check=True, capture_output=True, text=True)
        subprocess.run(["git", "push"], check=True, capture_output=True, text=True)

        print("✅ 提交成功！速度拉满！")
    except subprocess.CalledProcessError as e:
        print(f"❌ 出错啦：{e.stderr}")

if __name__ == "__main__":
    git_auto_commit()