package com.alipay.config;

/* *
 *������AlipayConfig
 *���ܣ�����������
 *��ϸ�������ʻ��й���Ϣ������·��
 *�汾��3.3
 *���ڣ�2012-08-10
 *˵����
 *���´���ֻ��Ϊ�˷����̻����Զ��ṩ���������룬�̻����Ը����Լ���վ����Ҫ�����ռ����ĵ���д,����һ��Ҫʹ�øô��롣
 *�ô������ѧϰ���о�֧�����ӿ�ʹ�ã�ֻ���ṩһ���ο���

 *��ʾ����λ�ȡ��ȫУ����ͺ��������ID
 *1.������ǩԼ֧�����˺ŵ�¼֧������վ(www.alipay.com)
 *2.������̼ҷ���(https://b.alipay.com/order/myOrder.htm)
 *3.�������ѯ���������(PID)��������ѯ��ȫУ����(Key)��

 *��ȫУ����鿴ʱ������֧�������ҳ��ʻ�ɫ��������ô�죿
 *���������
 *1�������������ã������������������������
 *2���������������ԣ����µ�¼��ѯ��
 */

public class AlipayConfig {

	// �����������������������������������Ļ�����Ϣ������������������������������
	// ���������ID����2088��ͷ��16λ��������ɵ��ַ���
	public static String partner = "2088711353384690";
	// �̻���˽Կ
	public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMyj/cijkyiPCqOAYfcRzT678FAkahkIBQz2hFrX7qB0ox+JdpUkY2w3PNW5NUoV03Z3aUAA9N5m7MjIqPDabhMfoi0pzTPcSDHc+BWXrHEOWGZ+BRlV6oWs9Mh0/x5+61yrI2uiU4xDKdGa8httoBjMkl5XrONU7/b/iWVZpqvpAgMBAAECgYEAn3EubMM2ByXcDiQbMBuIpNZdmLOmjrY26TCxUu5nGFGIPywlXnKpnEk1dvgE0yJKTkUfliSopRfgaVgeePpXQ90qCw26sXGBlCNvPpmv8eUhoBkdmx1NzMqgbHDJgrzfrS6qsobqyo1stsiLPySOWmhAIM7iui1jpd4RLFYGNLkCQQD5V7UtnueC0Y3WoUmYadBEZrP7d+5Oo2xY3IGtB+ukVYhQ8hJmuhW/nRBGd7gEVFVto4lnouFeifKU6mhqKRAHAkEA0hq9LDYp4CQuJFfPNguSX+aUyUDkLtRrW4I8m45g5WHeggpwDmarL0vhGW3EEBP3zJNmaikbLgyj249sj3WIjwJAehUPiH69K7gekm/18MIeTt9aUE2wSKCLdBbDB01ReuzGCZv8ln+WFnN4fIgxeS0xhrUDmdKONhOkynTbGt9wEQJBAJH4ec2po9ZRzKz7RL8rsvpl3KEmMGJIun3NMgZxGOjE4i4+yjN3KR+m/DEKjdkNxm+fSNwgyv1nUsBMGQRiiAkCQGK0UZh4JUzINwH6s9xa3i2668SxO7+2YXUZ4iilesIDgULijtUh7cyKKT5t8rgHzwMBLpP1rJd6tydOb5flShg=";

	// ֧�����Ĺ�Կ�������޸ĸ�ֵ
	public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// �����������������������������������Ļ�����Ϣ������������������������������

	// �����ã�����TXT��־�ļ���·��
	public static String log_path = "D:\\sjw_log\\alipay\\";

	// �ַ������ʽ Ŀǰ֧�� gbk �� utf-8
	public static String input_charset = "gbk";

	// ǩ����ʽ �����޸�
	public static String sign_type = "RSA";

}
