from selenium.webdriver.common.by import By
from selenium.webdriver.edge.service import Service
from selenium import webdriver
from selenium.webdriver.edge.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# 配置Edge浏览器
edge_options = Options()


# 设置浏览器参数以模拟真实手机环境
# 设置为微信浏览器环境
edge_options.add_argument('--app-version=7.0.20')  # 设置微信版本号
edge_options.add_argument('--mobile')  # 启用移动设备模拟
edge_options.add_argument('--touch-events=enabled')  # 启用触摸事件
edge_options.add_argument('--enable-viewport')  # 启用视口
edge_options.add_argument('--window-size=412,915')  # 设置窗口大小为标准手机尺寸
edge_options.add_argument('--force-device-scale-factor=2.0')  # 设置设备缩放比例
edge_options.add_argument('--enable-features=NetworkService,NetworkServiceInProcess')  # 启用网络服务
edge_options.add_argument('--disable-dev-shm-usage')  # 禁用/dev/shm使用
edge_options.add_argument('--disable-web-security')  # 禁用网页安全策略


edge_options.add_argument('--disable-blink-features=AutomationControlled')  # 关闭自动化标识
edge_options.add_argument('--disable-infobars')  # 禁用信息栏
edge_options.add_argument('--incognito')  # 启用隐身模式
edge_options.add_argument('--disable-popup-blocking')  # 禁用弹出窗口拦截
edge_options.add_argument('--disable-notifications')  # 禁用通知
edge_options.add_argument('--disable-save-password-bubble')  # 禁用保存密码提示
edge_options.add_experimental_option('excludeSwitches', ['enable-automation'])  # 去除浏览器提示自动化信息
edge_options.add_experimental_option('useAutomationExtension', False)  # 禁用自动化扩展

# 添加随机的 User-Agent
edge_options.add_argument('--user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 14_7_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Mobile/15E148 Safari/604.1')

wd = webdriver.Edge(service=Service("./msedgedriver"), options=edge_options)

arrays = [
    ["陈欣君", "21010219680216626X", "13888888888"],
    ["孔构组", "210102197601208587", "16666666666"],
    ["韩洋责", "21010219720318118X", "16000000006"]
]




def input_id_number(id_number):
    """增强版身份证输入函数"""
    wd.execute_script(f"arguments[0].innerText = '{id_number}';", cert_input)
    return cert_input.get_attribute('innerText') == id_number

for i, array in enumerate(arrays, 1):
    print(f"\n=== 正在处理第 {i} 组数据 ===")

    wd.execute_script("window.open('http://101.201.68.0/jnb7e7y379832ue889i38eie/1.html', '_blank');")
    #wd.execute_script("window.open('https://static.jnb.icbc.com.cn/ICBC/ICBCCOIN/roccentry.html', '_blank');")
    wd.switch_to.window(wd.window_handles[-1])

    # 等待用户按回车继续
    input("按回车键继续...")


    # 等待页面加载完成
    while True:
        try:
            # 检查页面是否包含特定元素来判断加载完成
            WebDriverWait(wd, 1).until(
                EC.presence_of_element_located((By.CSS_SELECTOR, 'body > div.top-wrapper'))
            )
            # 如果找到元素,说明页面加载完成,跳出循环
            break
        except:
            # 如果没找到元素,等待3秒后继续检查
            time.sleep(3)

    # 等待用户按回车继续
    input("按回车键继续...")



    try:
        # 姓名输入---找到表单并保证表单属于可见状态
        name_input = WebDriverWait(wd, 0).until(
            EC.presence_of_element_located((By.CSS_SELECTOR,
              'body > div.top-wrapper > div > div > div.page-custInfoOrder > div:nth-child(1) > div.page-custInfo-content > div.page-custInfo-content-input > div > input[type=text]'))
        )
        name_input.send_keys(array[0])
        print(f"验证已输入姓名: {name_input.get_attribute('value')}")

        # 身份证输入-找到身份证表单并保证表单属于可见状态
        cert_input = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.ID, 'certNoInput'))
        )
        cert_input.click()

        # 调用 input_id_number 函数检查身份证号码是否有效
        if input_id_number(array[1]):
            # 如果手机号码有效，打印成功信息
            print(f"√ 成功输入身份证号码: {array[1]}")
        else:
            # 如果手机号码无效，打印失败信息
            print(f"× 身份证号码输入失败: {array[1]}")
            # 终止当前循环的剩余部分，继续下一次循环
            continue
        #身份证结束

        # 手机号码输入-找到手机号码表单并保证表单属于可见状态
        tel_input = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.ID, 'telInput'))
        )
        tel_input.click()

        # 执行JavaScript来设置手机号码
        wd.execute_script(f"arguments[0].innerText = '{array[2]}';", tel_input)

        # 验证手机号码是否输入成功
        if tel_input.get_attribute('innerText') == array[2]:
            print(f"√ 成功输入手机号码: {array[2]}")
        else:
            print(f"× 手机号码输入失败: {array[2]}")
            continue
        #手机号结束

        #选择省份
        # 找到并点击省份选择框
        province_select = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.ID, '省份'))
        )
        province_select.click()

        # 选择天津市选项
        tianjin_option = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, "#省份 option[value='河北省']"))
        )
        tianjin_option.click()
        # 选择省份结束



        #选择城市
        # 找到并点击城市选择框
        city_select = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.ID, '城市'))
        )
        city_select.click()

        # 选择天津市选项
        tianjin_city_option = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, "#城市 option[value='廊坊市']"))
        )
        tianjin_city_option.click()
        #选择城市结束


        #选择区县
        # 找到并点击区县选择框
        qu_select = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.ID, '区县'))
        )
        qu_select.click()

        # 选择天津市选项
        jinnan_qu_option = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, "#区县 option[value='三河市']"))
        )
        jinnan_qu_option.click()
        #选择城市结束


        #选择网点
        # 找到并点击网点选择框
        wangdian_select = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.ID, '网点'))
        )
        wangdian_select.click()

        # 选择网点选项
        # 获取所有网点选项
        options = wd.find_elements(By.CSS_SELECTOR, "#网点 option")

        # 定义关键词列表
        positive_keywords = ['三河','燕郊']
        negative_keywords = ['北辰', '宝坻', '武清']

        # 提取数字的函数
        def extract_number(text):
            import re
            numbers = re.findall(r'\d+', text)
            return int(numbers[0]) if numbers else 0

        # 检查是否包含指定关键词
        def contains_keywords(text, keywords):
            return any(keyword in text for keyword in keywords)

        # 初始化最佳选项
        selected_option = None
        max_number = 0
        threshold = 100

        # 循环直到找到合适的选项或降低到100
        while threshold >= 10 and not selected_option:
            for option in options:
                text = option.text
                number = extract_number(text)

                # 条件1：数字大于阈值且包含正向关键词
                if number >= threshold and contains_keywords(text, positive_keywords):
                    selected_option = option
                    break

                # 条件2：更新最大数字的选项（不包含负向关键词）
                if number > max_number and not contains_keywords(text, negative_keywords):
                    max_number = number
                    max_option = option

            # 如果没找到符合条件1的选项，使用条件2的结果
            if not selected_option and max_number > 0:
                selected_option = max_option
                break

            # 降低阈值继续查找
            threshold -= 10

        # 如果仍未找到合适选项，选择第一个可用选项
        if not selected_option and options:
            selected_option = options[0]

        # 点击选中的选项
        if selected_option:
            selected_option.click()
        #选择网点结束

        #输入数量
        # 数量输入-找到数量表单并保证表单属于可见状态
        num_input = WebDriverWait(wd, 0).until(
            EC.presence_of_element_located((By.CSS_SELECTOR,
                '#bookNum > div:nth-child(1) > div.page-stockInfo-content-bookNum > div.page-stockInfo-content-bookNum-input > div > input'))
        )
        num_input.send_keys('20')
        time.sleep(0.2)

        # 验证数量是否输入成功------纪念币可以预约20，纪念钞只能预约10
        if num_input.get_attribute('value') == '20':
            print("√ 成功输入数量: 20")
        else:
            print("× 数量输入失败")
            continue
        #输入数量结束


        #选择已阅读
        # 找到并点击已阅读复选框
        read_checkbox = WebDriverWait(wd, 0).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR,
                'body > div.top-wrapper > div > div > div.page-protocol > div:nth-child(2)'))
        )
        read_checkbox.click()
        time.sleep(0.2)
        #选择已阅读结束
    except Exception as e:
        print(f"! 处理第 {i} 组数据时出错: {str(e)}")
        continue

input("\n所有操作完成，按回车键退出...")
wd.quit()
