package com.zb.service.image.nc;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import com.zb.common.utils.DateUtil;
import com.zb.service.cloud.StorageService;
import com.zb.service.image.BaseTool;
import com.zb.service.image.SimplePositions;
import com.zb.service.image.ZbFont;
import com.zb.service.image.i.OneDraw;

public class MeiRiYiQianTool extends BaseTool implements OneDraw{
	public MeiRiYiQianTool(StorageService storageService) {
		super(storageService);
	}

	public MeiRiYiQianTool() {
	}

	public static void main(String[] args) throws IOException {
		String tmpPath0 = "287-抽签.png";
		StorageService storageService = new StorageService();
		MeiRiYiQianTool tyt = new MeiRiYiQianTool(storageService);
		tyt.isDebug(true);
		System.out.println(tyt.draw("都点逼", tmpPath0,"3888"));
	}
	
	
	static String shenQian[] =
			
			{"  您抽到的是第○○一签 "+"\t"+""+"\t"+""+"\t"+"天门一挂榜，预定夺标人，马嘶芳草地，秋高听鹿鸣。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉，事业、财运、健康、婚姻均顺遂。",
			"  您抽到的是第○○二签 "+"\t"+""+"\t"+""+"\t"+"地有神，甚威灵，兴邦辅国，尊主庇民。 "+"\t"+""+"\t"+""+"\t"+""+"\t"+"神护签，言行循规蹈矩，事业上必有贵人相助。",
			"  您抽到的是第○○三签 "+"\t"+""+"\t"+""+"\t"+"长安花不可及，春风中马蹄疾。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"惕励不可好高骛远，目前情况不理想。",
			"  您抽到的是第○○四签"+"\t"+""+"\t"+""+"\t"+"春花娇媚，不禁雨打风飘，秋菊幽芳，反耐霜 雪傲。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"勉惕类，诸事无法顺利，踏实或有可望。",
			"  您抽到的是第○○五签"+"\t"+""+"\t"+""+"\t"+"春雷震，夏风巽，猛虎惊，风云会合，救济苍生。 "+"\t"+""+"\t"+""+"\t"+""+"\t"+"否极泰来，外旱逢甘雨，出外旅游亦不宜。",
			"  您抽到的是第○○六签"+"\t"+""+"\t"+""+"\t"+"非玄非奥，非浅非深，一个妙道，着意搜寻。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"心性勿求切，着意搜寻，必能如愿。",
			"  您抽到的是第○○七签"+"\t"+""+"\t"+""+"\t"+"君须悟，勿误疑，有平路，任驱驰。 "+"\t"+""+"\t"+""+"\t"+""+"\t"+"应机警应变，始能左右逢源。",
			"  您抽到的是第○○八签"+"\t"+""+"\t"+""+"\t"+"虎恋高山别有机，众人目下尚狐疑。 "+"\t"+""+"\t"+""+"\t"+""+"\t"+"目前无人能识，日後必受众人赞颂。",
			"  您抽到的是第○○九签"+"\t"+""+"\t"+""+"\t"+"贵客相逢更可期，庭前枯木凤来仪。 "+"\t"+""+"\t"+""+"\t"+""+"\t"+"命中得遇贵人相助，未婚男子可得佳侣。",
			"  您抽到的是第○一十签"+"\t"+""+"\t"+""+"\t"+"嘹呖征鸿独出群，高飞羽翼更纠纷。 "+"\t"+""+"\t"+""+"\t"+""+"\t"+"才气出众，无奈受制。",
			"  您抽到的是第○一一签"+"\t"+""+"\t"+""+"\t"+"无踪又无迹，远近均难觅，平地起风波，似笑还成泣。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"诸事不利。",
			"  您抽到的是第○一二签"+"\t"+""+"\t"+""+"\t"+"神黯黯，意悠悠，收却线，莫下钩。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"诸事暂且打住，免有失误，後悔莫及。",
			"  您抽到的是第○一三签"+"\t"+""+"\t"+""+"\t"+"得意宜逢妇，前程去有缘，利名终有望，三五月团圆。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉，或有殊遇、喜事。",
			"  您抽到的是第○一四签"+"\t"+""+"\t"+""+"\t"+"鼎沸起风波，孤舟要渡河，巧中却藏拙，人事转蹉跎。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不吉，须防树大招风烈。",
			"  您抽到的是第○一五签"+"\t"+""+"\t"+""+"\t"+"意在闲中信未来，故人千里自徘徊。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一切期待，均有可得。",
			"  您抽到的是第○一六签"+"\t"+""+"\t"+""+"\t"+"心和同，事知同，门外好施功，交加事有终。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，不合者必复合。",
			"  您抽到的是第○一七签"+"\t"+""+"\t"+""+"\t"+"欲行还止，徘徊不已，藏玉怀珠，寸心千里。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"宜下定决心，完成大业。",
			"  您抽到的是第○一八签"+"\t"+""+"\t"+""+"\t"+"心戚戚，口啾啾，一番思虑一番忧，说了休时又不休。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不利，结局恐难有望。",
			"  您抽到的是第○一九签"+"\t"+""+"\t"+""+"\t"+"不远不近，似易似难，等闲一事，云中笑看。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"平平，成败利钝淡然处之。",
			"  您抽到的是第○二十签"+"\t"+""+"\t"+""+"\t"+"桃李谢春风，西飞又复东，家中无意绪，船在浪涛中。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"栽种不得其时，去向漂流不定，家庭亦有风波。",
			"  您抽到的是第○二一签"+"\t"+""+"\t"+""+"\t"+"一水远一水，一山旋一山，水穷山尽处，名利不为难。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"须费尽九牛二虎之力始克有成。",
			"  您抽到的是第○二二签"+"\t"+""+"\t"+""+"\t"+"事相扶，在半途，翻覆终可免，风波一点无。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"目前不顺，惟亦不致有风险。",
			"  您抽到的是第○二三签"+"\t"+""+"\t"+""+"\t"+"喜喜喜，春风生桃李，不用强忧煎，明月人千里。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"喜讯已到，故人安然无恙，健康痊愈在即。",
			"  您抽到的是第○二四签"+"\t"+""+"\t"+""+"\t"+"意孜孜，心戚戚，要平安，防出入。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"谨言慎行，始得平安。",
			"  您抽到的是第○二五签"+"\t"+""+"\t"+""+"\t"+"见不见，也防人背面，遇不遇，到底无凭据。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"应防来自西方之暗箭，所问之事尚无徵候。",
			"  您抽到的是第○二六签"+"\t"+""+"\t"+""+"\t"+"一番桃李一番春，谁识当初气象新。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"或可任新职，经营事业以农林渔牧为宜。",
			"  您抽到的是第○二七签"+"\t"+""+"\t"+""+"\t"+"莫怪我见错，心性自成疴，偏僻不通心，真人却不魔。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不可固执己见，心性偏执者，宜修养心性。",
			"  您抽到的是第○二八签"+"\t"+""+"\t"+""+"\t"+"禄马交驰，男儿得志时，行程早办，荣归乐期颐。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉祥，若卜仕途宦海之事，尤称合适。",
			"  您抽到的是第○二九签"+"\t"+""+"\t"+""+"\t"+"了却心头事，三生夙有缘，香开十里桂，移步入天边。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉大利。",
			"  您抽到的是第○三十签"+"\t"+""+"\t"+""+"\t"+"缘黄阁白了头，毕竟成何济。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"攀权附贵者宜回头，顶天立地重新做人。",
			"  您抽到的是第○三一签"+"\t"+""+"\t"+""+"\t"+"离别间，虽不易，同伴行，犹不滞。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"平平，创业须结伴。",
			"  您抽到的是第○三二签"+"\t"+""+"\t"+""+"\t"+"乐之极忧将至，巽兑分明吉与凶。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不祥。",
			"  您抽到的是第○三三签"+"\t"+""+"\t"+""+"\t"+"历过波涛三五重，谁知浪静又无风。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"平平。",
			"  您抽到的是第○三四签"+"\t"+""+"\t"+""+"\t"+"缺月又重圆，枯枝色更鲜，一条夷坦路，翘首望青天。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉昌。",
			"  您抽到的是第○三五签"+"\t"+""+"\t"+""+"\t"+"行路难行路难，今日方知行路难。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一向平顺，今突生险，惟必可过关。",
			"  您抽到的是第○三六签"+"\t"+""+"\t"+""+"\t"+"春景明，春色新，春意傍水生。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"事业发达，健康良好，性情豁达，交游广阔。",
			"  您抽到的是第○三七签"+"\t"+""+"\t"+""+"\t"+"万马归元，千猿朝洞，虎伏龙降，道高德重。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"须以德化人。",
			"  您抽到的是第○三八签"+"\t"+""+"\t"+""+"\t"+"黄鹂报上林，春色鲜明，提鞭快着，马上速行程。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"做任何事，目前时机最佳。",
			"  您抽到的是第○三九签"+"\t"+""+"\t"+""+"\t"+"大肆放灵丹，救人行万千，到头登彼岸，渡过入仙班。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"行善济人，必得善果。",
			"  您抽到的是第○四十签"+"\t"+""+"\t"+""+"\t"+"一带水，碧澄澄，舟住江上，月到天心；稳步其中。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"塞翁失马，焉知非福。",
			"  您抽到的是第○四一签"+"\t"+""+"\t"+""+"\t"+"桃李舒姘，春光鲜丽，良辰美景君须记。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉祥，命中注定事事顺利。",
			"  您抽到的是第○四二签"+"\t"+""+"\t"+""+"\t"+"隐中显，显中微，个中有玄机，参得透了，直上仙梯。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"是否突破就看能否参透玄机。",
			"  您抽到的是第○四三签"+"\t"+""+"\t"+""+"\t"+"无上去，在前头，回头一悟，绳缰好收。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"即改弦易辙，另觅可行之路。",
			"  您抽到的是第○四四签"+"\t"+""+"\t"+""+"\t"+"四顾无门路，桃源路可通，修炼成正果，万岁寿如松。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"平平，只要多磨练、苦修，日後自成。",
			"  您抽到的是第○四五签"+"\t"+""+"\t"+""+"\t"+"不用忙，不用慌，自有驻足乡。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"做事沈着稳定，自有容身之地。",
			"  您抽到的是第○四六签"+"\t"+""+"\t"+""+"\t"+"奔波一世，总是虚浮，无常一到万事休，急早回头。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一生争名夺利，最後化为乌有，回头是岸。",
			"  您抽到的是第○四七签"+"\t"+""+"\t"+""+"\t"+"真真真，人不识，真真真，神有灵。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"诸事假象蒙蔽认清真象，转入正途。",
			"  您抽到的是第○四八签"+"\t"+""+"\t"+""+"\t"+"走尽天涯，风霜历遍，不如问人三天，渐渐有回首见。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"冒目躁进必白费功夫，应就教贤达。",
			"  您抽到的是第○四九签"+"\t"+""+"\t"+""+"\t"+"沈沈疴染，不见天心，雷门一震，体健身轻。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"兴隆有望。",
			"  您抽到的是第○五十签"+"\t"+""+"\t"+""+"\t"+"财马两匆忙，官禄有定方，猪羊牛犬，自去主张。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"顺其自然。",
			"  您抽到的是第○五一签"+"\t"+""+"\t"+""+"\t"+"空空空，空里得成功，蟠桃千载熟，不怕五更风。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大器晚成。",
			"  您抽到的是第○五二签"+"\t"+""+"\t"+""+"\t"+"愁脸放，笑颜开，秋月挂高台，人从千里来。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉祥，环境已转佳境。",
			"  您抽到的是第○五三签"+"\t"+""+"\t"+""+"\t"+"须着力，莫远游，长竿钓向蟾蜍窟，直欲云中得巨鳌。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"凡事须全力以赴，时运来临，愿望自然可成。",
			"  您抽到的是第○五四签"+"\t"+""+"\t"+""+"\t"+"无踪无迹，远近难觅，旱海行舟，空劳费力。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"诸事难成。",
			"  您抽到的是第○五五签"+"\t"+""+"\t"+""+"\t"+"细雨蒙蒙湿，江边路不通，道途音信远，凭仗借东风。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"时运欠佳，惟有依靠仅有之一线生机。",
			"  您抽到的是第○五六签"+"\t"+""+"\t"+""+"\t"+"平地起云烟，时下未能安，高处觅姻缘。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事现时现地均难有成，波折重重。",
			"  您抽到的是第○五七签"+"\t"+""+"\t"+""+"\t"+"正直宜守，妄动生灾，利通名达，叶落花开。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"应坚守本位，正直无欺。",
			"  您抽到的是第○五八签"+"\t"+""+"\t"+""+"\t"+"君子升，小人阻，征战生离苦。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事波折连连，须稳妥安排为宜。",
			"  您抽到的是第○五九签"+"\t"+""+"\t"+""+"\t"+"纷纷复纷纷，欷 独掩门， 眉望灯火，伴我坐黄昏。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"须守时耐运，待机而行，不可强求。",
			"  您抽到的是第○六十签"+"\t"+""+"\t"+""+"\t"+"红颜美，休挂怀，人在车中，舟行水里。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"戒之在色，凡事不可存奢望。",
			"  您抽到的是第○六一签"+"\t"+""+"\t"+""+"\t"+"桥已断，路不通，登舟理楫，又遇狂风。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大不吉，凡事宜慎。",
			"  您抽到的是第○六二签"+"\t"+""+"\t"+""+"\t"+"深潭月，照镜影，一场空，安报信。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"水底月，镜中花，花蓝打水一场空。",
			"  您抽到的是第○六三签"+"\t"+""+"\t"+""+"\t"+"湖海意悠悠，烟波下钓钩，若逢龙与兔，名利一齐周。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"示人胸襟以阔，达观为怀，自可名利双收。",
			"  您抽到的是第○六四签"+"\t"+""+"\t"+""+"\t"+"物不牢，人断桥，重整理，慢心高。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事应重新来过，如以笃实之态度为之，或有可成。",
			"  您抽到的是第○六五签"+"\t"+""+"\t"+""+"\t"+"入而易，出而难，恹恹到再三，交加意不堪。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"请神容易送神难。",
			"  您抽到的是第○六六签"+"\t"+""+"\t"+""+"\t"+"事迟志速，而且反覆，直待岁寒，花残果熟。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"若时机未熟，但凭己力，尚难成就。",
			"  您抽到的是第○六七签"+"\t"+""+"\t"+""+"\t"+"乘马前进，所求吉贞，随时谐美，缺月重明。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一鼓作气，勇往直前。",
			"  您抽到的是第○六八签"+"\t"+""+"\t"+""+"\t"+"舟离古渡月离云，人出潼关好问津。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签主外，出远行有利。",
			"  您抽到的是第○六九签"+"\t"+""+"\t"+""+"\t"+"野塘雨过月如钩，梦断邯郸眉黛愁。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"知足常乐，随遇而安，否则梦境难圆之慨。",
			"  您抽到的是第○七十签"+"\t"+""+"\t"+""+"\t"+"美有堪，堪有美，始有终，终有始。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"为人追求真善美方为正途，凡事必以始终一贯精神为之。",
			"  您抽到的是第○七一签"+"\t"+""+"\t"+""+"\t"+"湖海悠悠，孤舟浪头，来人未渡，残照山楼。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事尚在虚无飘渺间，当前处境未开情境苍茫。",
			"  您抽到的是第○七二签"+"\t"+""+"\t"+""+"\t"+"深户要牢扃，提防暗里人，莫言无外事，纵好定遭 。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"无事须当有事，有事更加防范。",
			"  您抽到的是第○七三签"+"\t"+""+"\t"+""+"\t"+"江海悠悠，烟波下钩，六鳌连获，歌笑中流。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签大吉，凡谋事、创业必有望。",
			"  您抽到的是第○七四签"+"\t"+""+"\t"+""+"\t"+"欲济未济，欲求强求，心无一定，一车两头。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"三心二意，有违常理，动弹不得。",
			"  您抽到的是第○七五签"+"\t"+""+"\t"+""+"\t"+"一得一虑，退後欲先，路通大道，心自安然。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得意之时勿忘形，，不行歪路，心境自然安适。",
			"  您抽到的是第○七六签"+"\t"+""+"\t"+""+"\t"+"难难难，忽然平地起波澜。                 "+"\t"+""+"\t"+""+"\t"+""+"\t"+"机遇欠佳时，一切左右逢源，得心应手。",
			"  您抽到的是第○七七签"+"\t"+""+"\t"+""+"\t"+"心有馀，力不足，倚仗春风，一歌一曲。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事，结果仍称圆满。",
			"  您抽到的是第○七八签"+"\t"+""+"\t"+""+"\t"+"身不安，心不安，动静两三番，终朝事必叹。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"问健康，需多所奔走，需努力。",
			"  您抽到的是第○七九签"+"\t"+""+"\t"+""+"\t"+"事了物未了，人圆物未圆，要知端的信，日影上琅 。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"人、事问题，均了断并圆满解决。",
			"  您抽到的是第○八十签"+"\t"+""+"\t"+""+"\t"+"木向阳春发，三阴又伏根，樵夫不知道，砍去作柴薪。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不可暴殄天物。",
			"  您抽到的是第○八一签"+"\t"+""+"\t"+""+"\t"+"一月缺，一镜缺，不团圆，无可说。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事自属不圆满，恐亦无转机。",
			"  您抽到的是第○八二签"+"\t"+""+"\t"+""+"\t"+"车马到临，旌旗隐隐月分明，招安讨叛，永大前程。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签如问战略戎事大吉，问事业则大发。",
			"  您抽到的是第○八三签"+"\t"+""+"\t"+""+"\t"+"我何宿，我何宿，海东河北成名录。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"选择努力方向，如能吸取前贤经验，必能事半功倍。",
			"  您抽到的是第○八四签"+"\t"+""+"\t"+""+"\t"+"金鳞入手，得还防走，若论周旋，谨言缄口。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"警惕吾人谨言少语，须防上当受骗。",
			"  您抽到的是第○八五签"+"\t"+""+"\t"+""+"\t"+"倾一 ，展愁眉，天地合，好思为。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"愁闷事在杯酒中尽释，夫妻间之不合应尽快化解。",
			"  您抽到的是第○八六签"+"\t"+""+"\t"+""+"\t"+"野鬼张弧射主人，暗中一箭鬼魂惊。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者如为主管，恐有欺害下属之事。",
			"  您抽到的是第○八七签"+"\t"+""+"\t"+""+"\t"+"福星照，吉宿临，青天有日见天真，龙飞下载到明庭。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签吉利，全属福佑德庇之赞。",
			"  您抽到的是第○八八签"+"\t"+""+"\t"+""+"\t"+"独钓寒潭，中途兴阑，水寒鱼不饵，空载月明还。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不祥，所处环境欠佳，所求未如理想，反生阻碍。",
			"  您抽到的是第○八九签"+"\t"+""+"\t"+""+"\t"+"不归一，劳心力，贵人旁，宜借力。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事意见纷陈，如不借助有力者，恐要白费心力。",
			"  您抽到的是第○九十签"+"\t"+""+"\t"+""+"\t"+"云尽月当中，光辉到处通，路途逢水顺，千里快如风。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"转机交运之兆，时下机运甚佳，所问大吉大利。",
			"  您抽到的是第○九一签"+"\t"+""+"\t"+""+"\t"+"剑戟列山林，盗贼必来侵，败走擒搜定，封侯荫子孙。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"激烈竞争中，妥为布置以待敌来，赢得胜利。",
			"  您抽到的是第○九二签"+"\t"+""+"\t"+""+"\t"+"岸阔水深舟易落，路遥山险步难行。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事宜向宽处打算，最後必能成功。",
			"  您抽到的是第○九三签"+"\t"+""+"\t"+""+"\t"+"拟欲迁而未可迁，提防喜处惹勾连。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"勿为喜事冲昏头，谨防欢乐引出麻烦。",
			"  您抽到的是第○九四签"+"\t"+""+"\t"+""+"\t"+"一人去，一人入，清风明月两相猜，获得金鳞下钓台。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"二人合作成功前互疑，惟风度尚在。",
			"  您抽到的是第○九五签"+"\t"+""+"\t"+""+"\t"+"喜未稳，悲已遭，大雨狂风吹古木，人人尽道不坚牢。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"世间悲欢无常，常能将人击倒。",
			"  您抽到的是第○九六签"+"\t"+""+"\t"+""+"\t"+"可以寄百里之命，可以托六尺之孤。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，交至友，得生死相许之配偶。",
			"  您抽到的是第○九七签"+"\t"+""+"\t"+""+"\t"+"报道上林，春色鲜明，提鞭快着，马上行程。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"亨通顺利，时运好转。",
			"  您抽到的是第○九八签"+"\t"+""+"\t"+""+"\t"+"鼠入土穴，最可安身，日中不见，静夜巡行。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"人各有其不同安身立命之所及最适当行动时机。",
			"  您抽到的是第○九九签"+"\t"+""+"\t"+""+"\t"+"打起平生志，西南好去游，腰缠十万贯，骑鹤上扬州。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，已功成名就。",
			"  您抽到的是第一○○签"+"\t"+""+"\t"+""+"\t"+"喜喜喜，终防否，获得骊龙颔下珠，忽然失却，还在水里。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"谨防有乐极生悲之事发生。",
			"  您抽到的是第一○一签"+"\t"+""+"\t"+""+"\t"+"国有贤士，廷无佞臣，干戈不用，常享太平。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"太平盛世，家庭和乐。",
			"  您抽到的是第一○二签"+"\t"+""+"\t"+""+"\t"+"泰来否已极，诸事莫忧心，但须 养元福。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"示人以心性修养，积功好义之道理。",
			"  您抽到的是第一○三签"+"\t"+""+"\t"+""+"\t"+"民乐业，官吏清，雍熙之世复见于今。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"应立求属下各安其位，各部门主管廉洁公正。",
			"  您抽到的是第一○四签"+"\t"+""+"\t"+""+"\t"+"安如泰山，稳如磐石，放胆前行，中通外直。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉大利。",
			"  您抽到的是第一○五签"+"\t"+""+"\t"+""+"\t"+"月中有丹桂，人终攀不着，云梯足下生，此际好落脚。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"如有匡助之力或引荐之人，也就获得成功机会。",
			"  您抽到的是第一○六签"+"\t"+""+"\t"+""+"\t"+"天间一孤雁，嘹唳叹离群，试问知君者，而今有几人。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，宜调整处世态度，以此为诫。",
			"  您抽到的是第一○七签"+"\t"+""+"\t"+""+"\t"+"红叶无颜色，凋零一夜风，邻鸡醒午梦，心事总成空。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"好运瞬即逝，理想忽受阻碍，办事难有成就。",
			"  您抽到的是第一○八签"+"\t"+""+"\t"+""+"\t"+"事如麻，理多错，日掩云中，空成耽搁。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事情况混乱，事多蹉跎，一无所成。",
			"  您抽到的是第一○九签"+"\t"+""+"\t"+""+"\t"+"勿上旧辙，甘驾新车，东西南北，稳步康衢。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"慎防勿蹈前车之覆辙，另作他图。",
			"  您抽到的是第一一○签"+"\t"+""+"\t"+""+"\t"+"夜梦被鼠惊，醒来不见人，终宵废寝，直到天明。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不祥之兆。",
			"  您抽到的是第一一一签"+"\t"+""+"\t"+""+"\t"+"秋霜肃，夏日炎，新花鲜了旧花淹。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"凡事靠自己，依赖别人不可靠。",
			"  您抽到的是第一一二签"+"\t"+""+"\t"+""+"\t"+"未展英雄志，驰驱不惮劳，敢将休咎卜，西北夺前标。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不避劳苦，慎选致力方向。",
			"  您抽到的是第一一三签"+"\t"+""+"\t"+""+"\t"+"疴染沈沈，终日昏昏，雷门一震，体健身轻。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"如问疾病，暂难痊愈，尚需时日，以得名医。",
			"  您抽到的是第一一四签"+"\t"+""+"\t"+""+"\t"+"易非易，难非难，忽地起波澜，欢笑两三番。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之，惟会突起变化，所有问题迎刃而解。",
			"  您抽到的是第一一五签"+"\t"+""+"\t"+""+"\t"+"路不通，门闭塞，谨慎提防，云藏月黑。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不祥。",
			"  您抽到的是第一一七签"+"\t"+""+"\t"+""+"\t"+"珠玉走盘中，田园定阜丰，休言谋未遂，此去便亨通。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"努力必有代价，但须耐心等待。",
			"  您抽到的是第一一八签"+"\t"+""+"\t"+""+"\t"+"月已明，花再发，事悠悠，无不合。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，所问诸事均处於最佳状态，事事称心如意。",
			"  您抽到的是第一二○签"+"\t"+""+"\t"+""+"\t"+"事未宽，心不安，疑虑久，始安然。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事处在急於解决却未能解决之状况。",
			"  您抽到的是第一二二签"+"\t"+""+"\t"+""+"\t"+"止止止，有终有始，似月如花，守成而已。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"成败得失，莫不缘於因果循环之定数，不必强求。",
			"  您抽到的是第一二三签"+"\t"+""+"\t"+""+"\t"+"明月全圆，颜色欣然，风云相送，和合万年。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉大利。",
			"  您抽到的是第一二五签"+"\t"+""+"\t"+""+"\t"+"居下不亲上，人心易散离，事机终失一，凡百尽成灰。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"最後关键时刻，招致失败。",
			"  您抽到的是第一二七签"+"\t"+""+"\t"+""+"\t"+"虎伏在路途，行人莫乱呼，路旁须仔细，灾祸自然无。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"遇事小心仔细，否则有招致横祸之可能。",
			"  您抽到的是第一二八签"+"\t"+""+"\t"+""+"\t"+"和不和，同不同，翻云覆雨几成空，进退须防终少功。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不吉。",
			"  您抽到的是第一二九签"+"\t"+""+"\t"+""+"\t"+"东边事，西边成，风物月华明，高楼弄笛声。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，所谋之事於闲散自在中，不期然而有所成。",
			"  您抽到的是第一三一签"+"\t"+""+"\t"+""+"\t"+"浅水起风波，平地生荆棘，言语虑参商，犹恐无端的。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"即便慎言亦惹是非，故应少言，甚至不言。",
			"  您抽到的是第一三三签"+"\t"+""+"\t"+""+"\t"+"心已定，事何忧，金鳞已上钩，功名一网收。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉祥，所问诸事，心意已决，即可达成，同时功成名就。",
			"  您抽到的是第一三四签"+"\t"+""+"\t"+""+"\t"+"意迷己不迷，事宽心不宽，要知端的信，犹隔两重山。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"凡事未可乐观。",
			"  您抽到的是第一三七签"+"\t"+""+"\t"+""+"\t"+"荆棘生平地，风波起四方，倚栏惆怅望，无语对斜阳。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"或有烦心之事来临，内心忧闷无人谅解。",
			"  您抽到的是第一三八签"+"\t"+""+"\t"+""+"\t"+"谋已定，事何忧，照月上重楼，云中客点头。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大局安排已定，不必多作考虑。",
			"  您抽到的是第一三九签"+"\t"+""+"\t"+""+"\t"+"奇奇奇，地利与天时，灯花传信後，动静总相宜。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事，成败在於天时与地利。",
			"  您抽到的是第一四○签"+"\t"+""+"\t"+""+"\t"+"遇不遇，逢不逢，月沈海底，人在梦中。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事均在虚无飘渺中。",
			"  您抽到的是第一四一签"+"\t"+""+"\t"+""+"\t"+"暗中防霹雳，猜虑浑无实，转眼黑云收，拥出扶桑日。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所耽忧之事均属子虚乌有，很快就能真相大白。",
			"  您抽到的是第一四三签"+"\t"+""+"\t"+""+"\t"+"堪叹外边忧，更嗟门里闹，意绪更牵缠，心神亦颠倒。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不吉之兆。",
			"  您抽到的是第一四四签"+"\t"+""+"\t"+""+"\t"+"一重山一重水，风波道坦然，壶中有别天。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"如问机构内之事，则虽风波重重亦安。",
			"  您抽到的是第一四六签"+"\t"+""+"\t"+""+"\t"+"船棹中流急，花开春又离，事宁心不静，惹起许多疑。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"心情纷乱，办事虑深。",
			"  您抽到的是第一四七签"+"\t"+""+"\t"+""+"\t"+"可蓄可储，片玉寸珠，停停稳稳，前遇良图。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不可浪费，遇良机时始能大展鸿图。",
			"  您抽到的是第一四八签"+"\t"+""+"\t"+""+"\t"+"小子早趋庭，青云久问程，贵人来助力，花谢子还成。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"可望使其得到贵人相助，并实现愿望。",
			"  您抽到的是第一四九签"+"\t"+""+"\t"+""+"\t"+"一心两事，一事两心，新花枯树，直待交春。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者有心意不专。",
			"  您抽到的是第一五○签"+"\t"+""+"\t"+""+"\t"+"大事恐难图；残花不再鲜。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"时机已逝，时光已老，欲图大事，已不可能。",
			"  您抽到的是第一五一签"+"\t"+""+"\t"+""+"\t"+"莫道事无讹，其中进退多，桂轮圆又缺，光彩更揩磨。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"好事多磨。",
			"  您抽到的是第一五二签"+"\t"+""+"\t"+""+"\t"+"莫叹事迟留，休言不到头，长竿终入手，一钓上金钩。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"有耐心、恒心，最後终能如愿。",
			"  您抽到的是第一五三签"+"\t"+""+"\t"+""+"\t"+"事称应有忌，未为恐先踬，欲往且迟迟，还须借势力。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不必操之过急，须仰人相助，或有所成。",
			"  您抽到的是第一五四签"+"\t"+""+"\t"+""+"\t"+"足不安，舆不安，两两事相得，忧来却又欢。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"塞翁失马，焉知非福。",
			"  您抽到的是第一五五签"+"\t"+""+"\t"+""+"\t"+"鼎折足，车脱辐，有贵人，重整续。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事於开始非常不顺，命中有贵人相助，事情得以重整。",
			"  您抽到的是第一五八签"+"\t"+""+"\t"+""+"\t"+"平地起波澜，所求事日难，笑谈终有忌，同心事觉欢。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"目前机运欠佳，须与知心友朋共事，庶几无患。",
			"  您抽到的是第一五九签"+"\t"+""+"\t"+""+"\t"+"狂风吹起墨云飞，月在天心遮不得。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事或人，如天心之月。",
			"  您抽到的是第一六○签"+"\t"+""+"\t"+""+"\t"+"人倚楼，许多愁，澹然进步，事始无忧。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"独处深思，总觉不悦，若能眼光放远，自然无忧。",
			"  您抽到的是第一六一签"+"\t"+""+"\t"+""+"\t"+"一点着阳春，枯枝朵朵新，志专方遇合，切忌二三心。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"心志专一，略得提携即可步入佳境。",
			"  您抽到的是第一六二签"+"\t"+""+"\t"+""+"\t"+"道路迢遥，门庭闭塞，雾拥去兮，云开见日。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"应守时待运。",
			"  您抽到的是第一六三签"+"\t"+""+"\t"+""+"\t"+"鱼上钩，丝纶弱，收拾难，力再着。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"将得手之机会或财物，由於条件不济，须再加努力。",
			"  您抽到的是第一六四签"+"\t"+""+"\t"+""+"\t"+"相引更相牵，殷勤喜自然，施为无不利，愁事转团圆。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"故人际关系良好，事事均如意。",
			"  您抽到的是第一六六签"+"\t"+""+"\t"+""+"\t"+"上下不和同，劳而未有功，出门通大道，从此保初终。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"与其在一个不合之团体里，不如早退出。",
			"  您抽到的是第一六九签"+"\t"+""+"\t"+""+"\t"+"遍书前後事，艰险往来难，若得清风便，扁舟过远山。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事颇难如愿，须待天时。",
			"  您抽到的是第一七○签"+"\t"+""+"\t"+""+"\t"+"莫叹残花，花开枯树，屋头春意，喜笑嘻嘻。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，恐有失意事，惟转机已现。",
			"  您抽到的是第一七二签"+"\t"+""+"\t"+""+"\t"+"欲得月中兔，须凭桃李拂，高山来接引，双喜照双眉。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"要得到不易到手之物，非借不凡之力。",
			"  您抽到的是第一七三签"+"\t"+""+"\t"+""+"\t"+"事遂勿忧煎，春风喜自然，更垂三尺钩，得意获鳞鲜。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉，所问诸事均能如愿，且喜事连连。",
			"  您抽到的是第一七四签"+"\t"+""+"\t"+""+"\t"+"圆又缺，缺又圆，低低密密要周旋，时来始见缘。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"要求功德圆满，但凭个人造化。",
			"  您抽到的是第一七五签"+"\t"+""+"\t"+""+"\t"+"乘病马，上危坡，防失跌，见蹉跎。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"屋漏又遭连夜雨，船破偏遇当头风，衰透至极。",
			"  您抽到的是第一七六签"+"\t"+""+"\t"+""+"\t"+"两事已和同，轻舟遇便风，道迷人得意，歌唱急流中。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得意勿忘形，忘形恐有灾。",
			"  您抽到的是第一七七签"+"\t"+""+"\t"+""+"\t"+"白玉蒙尘，黄金埋土，久久光辉，也须人举。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"虽有才华却遭埋没，须待有心人来提拔重用。",
			"  您抽到的是第一七八签"+"\t"+""+"\t"+""+"\t"+"上接不稳，下接不和，相缠相扰，平地风波。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"无论在团体、在家庭或社会，上下皆不相容。",
			"  您抽到的是第一七九签"+"\t"+""+"\t"+""+"\t"+"背後笑嘻嘻，中行道最宜，所求终有望，不必皱双眉。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"心中所愿终可实现，何必愁眉深锁？",
			"  您抽到的是第一八○签"+"\t"+""+"\t"+""+"\t"+"憔悴无人问，林间听杜鹃，一声山月笛，千里泪涓涓。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签指一对恋人，因云山相阻，相思成疾。",
			"  您抽到的是第一八一签"+"\t"+""+"\t"+""+"\t"+"菱荷香里受深恩，桂魄圆时印绶新。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签如专就宦途而言，诚属上上大吉，青云直上。",
			"  您抽到的是第一八三签"+"\t"+""+"\t"+""+"\t"+"欲行还止，徘徊不已，动摇莫强，得止且止。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签示人，若三心二意去做一件事，出於勉强。",
			"  您抽到的是第一八六签"+"\t"+""+"\t"+""+"\t"+"三箭开云路，营求指日成，许多闲口语，翻作笑歌声。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签属机运亨通之签。",
			"  您抽到的是第一八七签"+"\t"+""+"\t"+""+"\t"+"休眷恋，误前程，终闹乱，出门庭。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"莫为意中人或原留之地恋恋不舍，而误未来大事。",
			"  您抽到的是第一八八签"+"\t"+""+"\t"+""+"\t"+"万里波涛静，一天风月闲，利名无阻隔，行路出重关。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉。",
			"  您抽到的是第一八九签"+"\t"+""+"\t"+""+"\t"+"渴望梅， 画饼，漫劳心，如捉影，遇虎龙，方可省。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，恐有幻想之习，应即改之。",
			"  您抽到的是第一九二签"+"\t"+""+"\t"+""+"\t"+"事若羁留，人不出头，往来闭塞，要见无有。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"从事任何事，以谨慎机敏为上。",
			"  您抽到的是第一九三签"+"\t"+""+"\t"+""+"\t"+"万里片帆转，波平浪不惊，行行无阻滞，远处更通津。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事一向稳妥顺利，更属定操胜券。",
			"  您抽到的是第一九五签"+"\t"+""+"\t"+""+"\t"+"鹤自云中出，人从月下归，新欢盈脸上，不用皱双眉。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉祥，无所不利。",
			"  您抽到的是第一九六签"+"\t"+""+"\t"+""+"\t"+"深潭鱼可钓，幽谷鸟可罗，只用久长心，不用生疑惑。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"具有耐心，任何事均可成，不必怀疑。",
			"  您抽到的是第一九七签"+"\t"+""+"\t"+""+"\t"+"进不安，退不可，上下相从，明珠一颗。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事，须相关诸人上下一心，始有成功之望。",
			"  您抽到的是第一九八签"+"\t"+""+"\t"+""+"\t"+"着着占先机，其中路不迷，目前无合意，乍免是和非。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一向事事称心，如今却不再如意，但是非可免。",
			"  您抽到的是第一九九签"+"\t"+""+"\t"+""+"\t"+"雀噪高枝上，行人古渡头，半途不了事，日暮转生愁。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"众说纷纭，莫衷一是。",
			"  您抽到的是第二○○签"+"\t"+""+"\t"+""+"\t"+"凿石得玉，淘沙得珠，眼前目下，何用踌躇。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者，时运特佳，做任何事都会有意外之收获。",
			"  您抽到的是第二○二签"+"\t"+""+"\t"+""+"\t"+"阆苑一时春，庭前花柳新，声传好信息，草木尽欣欣。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事均吉祥如意。",
			"  您抽到的是第二○四签"+"\t"+""+"\t"+""+"\t"+"事有喜，面有光，终始好商量，壶中日月长。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签主得贤妻，家庭和谐，生活幸福。",
			"  您抽到的是第二○五签"+"\t"+""+"\t"+""+"\t"+"暗去有明来，忧心事可谐，终须成一笑，目下莫疑猜。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"暗去明来，明去暗来，冥冥之中自有定数。",
			"  您抽到的是第二○六签"+"\t"+""+"\t"+""+"\t"+"宝镜无尘染，金貂已剪裁，也逢天意合，终不惹尘埃。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"好事成双，事事如意。",
			"  您抽到的是第二○七签"+"\t"+""+"\t"+""+"\t"+"和合事，笑谈成，喜音在半程，平步踏青云。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉，双喜临门人生至乐。",
			"  您抽到的是第二○八签"+"\t"+""+"\t"+""+"\t"+"花残月缺，镜破钗分，休来休往，事始安宁。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大不吉祥，诸事不宜。",
			"  您抽到的是第二一○签"+"\t"+""+"\t"+""+"\t"+"万里好江山，风沙尽日间，已吞钩上饵，何必遇波澜。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"即将达成之希望亦被预料不到之状况破坏无遗。",
			"  您抽到的是第二一一签"+"\t"+""+"\t"+""+"\t"+"双燕衔书舞，指日一齐来，寂寞淹留客，从兹下钓台。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所期盼之事或人，已指日可待。",
			"  您抽到的是第二一二签"+"\t"+""+"\t"+""+"\t"+"望去几重山，高深渐可攀，举头天上看，明月在人间。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事看似高深莫测，终有明白一天。",
			"  您抽到的是第二一三签"+"\t"+""+"\t"+""+"\t"+"用之则行，舍之则藏，一骑出重关，佳音咫尺间。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"用人应用有志节及才干之人，很快即有成就。",
			"  您抽到的是第二一四签"+"\t"+""+"\t"+""+"\t"+"积德施功有子孙， 牛祭神及西邻。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"行善积德，泽被乡里，将来必有好报。",
			"  您抽到的是第二一七签"+"\t"+""+"\t"+""+"\t"+"有一人，获一鹿，事团圆，门外索。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问事必成，但须防後患，得配偶则第三者纠 不放。",
			"  您抽到的是第二一八签"+"\t"+""+"\t"+""+"\t"+"汝往无攸利，花开又及秋，严霜物荐至，退步不存留。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不祥。",
			"  您抽到的是第二一九签"+"\t"+""+"\t"+""+"\t"+"新月为钩，清风作线，举网烟波，锦鳞易见。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"佳，凡事得心应手，左右逢源。",
			"  您抽到的是第二二○签"+"\t"+""+"\t"+""+"\t"+"先关锁，续提防，小节不知戒，因循成大殃。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不怕一万，只怕万一，星星之火，亦可燎原。",
			"  您抽到的是第二二一签"+"\t"+""+"\t"+""+"\t"+"燕语莺啼，花开满院，倚栏春睡觉，无语 愁颜。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"时运已转，凡事可以顺利进行无阻。",
			"  您抽到的是第二二二签"+"\t"+""+"\t"+""+"\t"+"劳心劳心，劳心有成，清风借力，欢笑前程。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"随时留心、深思，自必於事有补，前途开朗。",
			"  您抽到的是第二二四签"+"\t"+""+"\t"+""+"\t"+"玉石犹终昧，那堪小悔多，终无咎，笑呵呵。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事由於当事人之昏昧，小错很多，但并无大碍。",
			"  您抽到的是第二二七签"+"\t"+""+"\t"+""+"\t"+"青毡空守旧，枝上巢生风，莫为一时喜，还疑此象凶。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不祥，应加谨防，且有风雨飘摇之险。",
			"  您抽到的是第二二九签"+"\t"+""+"\t"+""+"\t"+"上下和，忧愁决，千嶂云，一轮月。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"家和万事兴。",
			"  您抽到的是第二三○签"+"\t"+""+"\t"+""+"\t"+"玉出昆冈石，舟离古渡滩，行藏终有望，用舍不为难。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"某事或某人之底细即可水落石出。",
			"  您抽到的是第二三一签"+"\t"+""+"\t"+""+"\t"+"目下意难舒，有客来徐徐，金车虽历险，吝必有终与。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"阁下心事纠结不开，日後处境将留怅恨。",
			"  您抽到的是第二三二签"+"\t"+""+"\t"+""+"\t"+"可以寄，可以托，事迟迟，无舛错。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事可以托付他人去办，不过进行缓慢，好在尚不至出错。",
			"  您抽到的是第二三三签"+"\t"+""+"\t"+""+"\t"+"恐惧忧煎，皆在目前，若逢明鉴，指破空传。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事忧患深重，若得人指点迷津，或可化险为夷。",
			"  您抽到的是第二三四签"+"\t"+""+"\t"+""+"\t"+"月掩云间，昏迷道路，云散月明，渐宜进步。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事一时遭遇困阻，不辨路径，须排除障碍後。",
			"  您抽到的是第二三六签"+"\t"+""+"\t"+""+"\t"+"临渊放钩，清绝点埃，巨鳌随得，不用疑猜。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"做了抉择之後就应耐心以待，不必患得患失。",
			"  您抽到的是第二三七签"+"\t"+""+"\t"+""+"\t"+"无中应有直，心事还成戚，云散月重圆，千里风帆急。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"平平，进行心中之事。",
			"  您抽到的是第二四○签"+"\t"+""+"\t"+""+"\t"+"道必坚心，坚心必道成，建功勋。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"勉人立身修业，勿图官宦之奢望，勿为功禄所贪恋。",
			"  您抽到的是第二四一签"+"\t"+""+"\t"+""+"\t"+"一念上天堂，一念入地狱，地狱天堂。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"荣耀或羞辱取决人之一念之间",
			"  您抽到的是第二四二签"+"\t"+""+"\t"+""+"\t"+"羊逸群，日对民，逢牛口，便咬人，一个君一个臣。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所显诗句近乎玄虚，不适用现代，不妨再卜一签。",
			"  您抽到的是第二四三签"+"\t"+""+"\t"+""+"\t"+"若是有缘人，一指便回首，执迷不悟者，屡引也不走。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"视问卜者是否有缘及个性是否执拗。",
			"  您抽到的是第二四四签"+"\t"+""+"\t"+""+"\t"+"月儿升东，清光可挹，万里无云，海天一碧。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"凡事均无阻塞，今後环境更趋有利。",
			"  您抽到的是第二四六签"+"\t"+""+"\t"+""+"\t"+"心月狐狸，迷惑世人，世人不察，延久倾身。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"世间有若干危害人体之嗜好，智者应以妖狐迷人为鉴。",
			"  您抽到的是第二四七签"+"\t"+""+"\t"+""+"\t"+"蓦地狂风起，大树尽掀扬，枝叶未凋零，培植终无恙。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"有惊无险。",
			"  您抽到的是第二四八签"+"\t"+""+"\t"+""+"\t"+"虚日旺相，法要推寻，四围旋绕，对敌冲营。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"勿为假相所欺，应多方深入探讨真相。",
			"  您抽到的是第二五○签"+"\t"+""+"\t"+""+"\t"+"女儿大，喜临门，嫁良人，添子孙，同拜受，感皇恩。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吉祥，更为称庆。",
			"  您抽到的是第二五一签"+"\t"+""+"\t"+""+"\t"+"木生火，口不噤，疯癫作症，寒热相侵。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大不利，应谨言慎行，以防不测。",
			"  您抽到的是第二五二签"+"\t"+""+"\t"+""+"\t"+"休休休，过了三年又六周，不猛省，祸到头。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"在近三年半左右，尽量减少兴革异动。",
			"  您抽到的是第二五三签"+"\t"+""+"\t"+""+"\t"+"槛栏起火，孽畜遭殃，预防得力，灭火成康。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"凡事先防范於未然，才能安全无虑。",
			"  您抽到的是第二五四签"+"\t"+""+"\t"+""+"\t"+"已遂心头愿，始知志气伸，三山须把握，频频定太平。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"事情办成後，仍应持谨慎戒惧之心。",
			"  您抽到的是第二五五签"+"\t"+""+"\t"+""+"\t"+"福星照映，桂子香闻，满天星斗，光耀逼人。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉大利，命中注定有神明护佑。",
			"  您抽到的是第二五七签"+"\t"+""+"\t"+""+"\t"+"死有日，生有时，何事慢踌躇，飘然一往，心上无疑。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"人生一切莫不有定数，对事不必过於计较。",
			"  您抽到的是第二六○签"+"\t"+""+"\t"+""+"\t"+"终身不习上，在世却枉然，轮回不能免，永落深坑堑。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"劝人不能昏 渡日。",
			"  您抽到的是第二六一签"+"\t"+""+"\t"+""+"\t"+"两个子女，同到齐行，阴阳和合，谋作欢欣。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者可望得一子一女，凡事求阴阳调和。",
			"  您抽到的是第二六二签"+"\t"+""+"\t"+""+"\t"+"中有玄机赋，鸡鸣方显露，猛然悟禅关，打破君门路。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事疑难未解，但很快就能悟出解决之道。",
			"  您抽到的是第二六三签"+"\t"+""+"\t"+""+"\t"+"数尾金鱼吞饵，丝竿钓了回头。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"意外之好运，有时不是福气反成负担。",
			"  您抽到的是第二六四签"+"\t"+""+"\t"+""+"\t"+"卯日儿出林，午时正福临，卯生於寅，方见天心。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"做任何事不能违背自然法则。",
			"  您抽到的是第二六五签"+"\t"+""+"\t"+""+"\t"+"日中不决，日到方明，一场好事，六耳同成。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"日子到了事情自明朗，应为三个人合力办成之好事。",
			"  您抽到的是第二六六签"+"\t"+""+"\t"+""+"\t"+"孤宿是妖星，猿猴及树精，入山遇此曜，迷了性和心。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签示人：孤独易於受惑。",
			"  您抽到的是第二六七签"+"\t"+""+"\t"+""+"\t"+"滴漏声催鸡唱，趱行人逐队放，晚渡关津，前程无量。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"全力以赴，必有所成。",
			"  您抽到的是第二六八签"+"\t"+""+"\t"+""+"\t"+"人在天涯外，久乏信音来，家人频望眼，草木畅胸怀。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所念之人，远在他乡，对其怀念殷切。",
			"  您抽到的是第二六九签"+"\t"+""+"\t"+""+"\t"+"这颗树下，一穴生成，若迁此土，福禄骈臻。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"凡命运亨通者，福泽依增 勿营华屋。",
			"  您抽到的是第二七○签"+"\t"+""+"\t"+""+"\t"+"躬耕陇亩，形神似劳，无拘无系，其乐陶陶。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"过不求人之生活，身体虽劳，精神却愉快。",
			"  您抽到的是第二七一签"+"\t"+""+"\t"+""+"\t"+"祸来见鬼，鬼病淹缠，金羊得路，身脱灾殃。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大祸临头，心被鬼怪的病淹没而纠缠不放。",
			"  您抽到的是第二七三签"+"\t"+""+"\t"+""+"\t"+"深山据猛虎，虎啸出山窝，扬威抖擞，何怕人多。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"雄壮威严，果敢勇往始可折服众人。",
			"  您抽到的是第二七四签"+"\t"+""+"\t"+""+"\t"+"三天门，四地户，军马齐奔，鸣鼓进步。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"吾人行事，宜先制定良策，方可致胜可能。",
			"  您抽到的是第二七五签"+"\t"+""+"\t"+""+"\t"+"山山山，山上建茅 ，不比人间栋宇，却如天上云昙。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"知足常乐。",
			"  您抽到的是第二七七签"+"\t"+""+"\t"+""+"\t"+"有子长，成水局，时遇火反发福。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"世事难以尽如人意，不是太过就是不及。",
			"  您抽到的是第二七九签"+"\t"+""+"\t"+""+"\t"+"蛰龙已出世，头角首生成，云兴雨泽，得济苍生。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉大利。",
			"  您抽到的是第二八○签"+"\t"+""+"\t"+""+"\t"+"火势薰天，天边尽赤，遇际水源，庶乎成格。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一旦发生意外灾祸，应有克制对策。",
			"  您抽到的是第二八四签"+"\t"+""+"\t"+""+"\t"+"六牛耕地，垦开无疆，收成结实，盈禀盈仓。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一分耕耘，一分收获，天下无侥幸而成之事。",
			"  您抽到的是第二八六签"+"\t"+""+"\t"+""+"\t"+"葵花向日，忠赤倾心，大开广厦，乐享太平。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者深受部属爱戴，使其事业鸿图大展。",
			"  您抽到的是第二八七签"+"\t"+""+"\t"+""+"\t"+"人不识仙，那有真诀，一入玄门，津津有益。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"进行一事，把握其中要领，方能得心应手。",
			"  您抽到的是第二八八签"+"\t"+""+"\t"+""+"\t"+"勿谓说话太沈，泥了就不成真。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事，面貌并不清朗，须用心去探求其内蕴。",
			"  您抽到的是第二八九签"+"\t"+""+"\t"+""+"\t"+"功名虽多实际，何如修炼成真，真身不朽，万载长春。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"与其汲汲於追求功名，不如修身养性。",
			"  您抽到的是第二九○签"+"\t"+""+"\t"+""+"\t"+"走走走，遇一狗，急思寻，可长久。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"处理事情上不宜忽略小节。",
			"  您抽到的是第二九二签"+"\t"+""+"\t"+""+"\t"+"火旺处要不疲，水深处要不呆。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"事情进行到最重要关头应慎思斟酌，方不致带来灾害。",
			"  您抽到的是第二九三签"+"\t"+""+"\t"+""+"\t"+"宾雁 湖地成陆，行建功勋早回程。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"见好就收。",
			"  您抽到的是第二九四签"+"\t"+""+"\t"+""+"\t"+"天上风，天边月，月白风清，两两相当。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"时运正佳，故凡求职、求偶、求学、求财等均有吉利。",
			"  您抽到的是第二九七签"+"\t"+""+"\t"+""+"\t"+"木长春天根干老，子实三秋枝叶凋。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"先经挫折磨练，然後其生命力必更坚强。",
			"  您抽到的是第二九八签"+"\t"+""+"\t"+""+"\t"+"何不伸首舒眉，反做蓬蒿到老。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"早起三光，晏起三荒，应及时努力，遇事不可因循不振。",
			"  您抽到的是第二九九签"+"\t"+""+"\t"+""+"\t"+"药饵真，服了宁，三剂後，足分明，神中神。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"任何事物，真即真假即假，一经多方考验，底细便明。",
			"  您抽到的是第三○○签"+"\t"+""+"\t"+""+"\t"+"三天曾结社，四海尽知名，长骑骏马，直入天庭。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"大吉大利，凡事左右逢源，得心应手。",
			"  您抽到的是第三○一签"+"\t"+""+"\t"+""+"\t"+"闲来夫子处，偶然遇一人，童颜鹤发，笑里生春。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"命中有吉人相助。",
			"  您抽到的是第三○三签"+"\t"+""+"\t"+""+"\t"+"汉水无情，蜀水澄清，黄河滚滚，四处烟尘。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"浊者自浊，清者自清。",
			"  您抽到的是第三○四签"+"\t"+""+"\t"+""+"\t"+"潜龙已受困，尚不见云兴，伫看云四合，飞去到天庭。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"求签者暂遇挫折，待时机成熟。",
			"  您抽到的是第三○五签"+"\t"+""+"\t"+""+"\t"+"此处滋味浓，浓艳不耐久，何如谈笑生风，倒好东奔西走。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"好花不常开，并另谋良图。",
			"  您抽到的是第三○六签"+"\t"+""+"\t"+""+"\t"+"这里有小人，切莫稍留停，忙打点。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事可能受到小人干扰破坏。",
			"  您抽到的是第三○七签"+"\t"+""+"\t"+""+"\t"+"龙生头角，将沛甘霖，六七八早，好济苍生。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"感应签，求职谋事创业等，遇龙年生者可得其助。",
			"  您抽到的是第三○八签"+"\t"+""+"\t"+""+"\t"+"太白现西南，龙蛇相竞逐，龙自飞上天，蛇却被刑戮。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签示人：善恶终有报。",
			"  您抽到的是第三○九签"+"\t"+""+"\t"+""+"\t"+"曾把树栽，也要待春来，东风袅袅，开遍花街。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"为某事劳心尽力，但须待时机及人助方有成。",
			"  您抽到的是第三一○签"+"\t"+""+"\t"+""+"\t"+"四十馀年苦已深，而今汝乐度光阴，莫筹论。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签劝人要能知足，并把握光阴享受人生。",
			"  您抽到的是第三一一签"+"\t"+""+"\t"+""+"\t"+"，到头处，亦成冰，急急回首，勿误前程。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问诸事做法上恐有问题，应即改弦更张另作安排。",
			"  您抽到的是第三一二签"+"\t"+""+"\t"+""+"\t"+"奇怪奇怪，前番来了，今番又来，谨慎提防。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事情况怪异，应提防生变，以免坏了大事。",
			"  您抽到的是第三一三签"+"\t"+""+"\t"+""+"\t"+"耕牛伏 ，辟土开疆，坐看收获，黍稷稻梁。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者有以逸待劳，坐享其成之机运。",
			"  您抽到的是第三一四签"+"\t"+""+"\t"+""+"\t"+"腰下佩青萍，步入金銮殿，覆护三山，千锤百链。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者功勋卓着，必为行业中顶尖人物。",
			"  您抽到的是第三一五签"+"\t"+""+"\t"+""+"\t"+"雏鸟飞高，出谷迁乔，龙神牙爪，变化海岛。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"初入社会难免饱受波涛翻滚之苦。",
			"  您抽到的是第三一七签"+"\t"+""+"\t"+""+"\t"+"大火炎炎，宜水相济，宝鼎丹成，掀天揭地。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不经意引发之灾难总有克制之法，往往一发不可收拾。",
			"  您抽到的是第三一八签"+"\t"+""+"\t"+""+"\t"+"铁索一条，未把孤舟系，金刀一下，早把头落地。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事宜以快刀斩乱麻方式处理，以杜後患。",
			"  您抽到的是第三一九签"+"\t"+""+"\t"+""+"\t"+"十二时中，紧急炼着，一刻少延，无处下脚。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"做任何事，均要抱着积极态度不懈不怠全力以赴。",
			"  您抽到的是第三二○签"+"\t"+""+"\t"+""+"\t"+"风起西南，红日当天，奇门妙诀，一掌能着。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"当时机成熟，谜题即可轻易揭开。",
			"  您抽到的是第三二一签"+"\t"+""+"\t"+""+"\t"+"万籁无声际，一月正当空，忽被云遮掩，皓魄反朦胧。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者有招人忌怨、暗算之危。",
			"  您抽到的是第三二二签"+"\t"+""+"\t"+""+"\t"+"一个知音，却在天边等，切勿因循，静夜当思省。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得签者为人处事有「曲过高和太寡」之情形。",
			"  您抽到的是第三二三签"+"\t"+""+"\t"+""+"\t"+"众犬相聚，砺齿咬牙，摇头摆尾，只顾看家。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签示人勿为小人群聚时之假象欺骗。",
			"  您抽到的是第三二五签"+"\t"+""+"\t"+""+"\t"+"鼠伏穴，本自宁，一露首，猫即跟，扬威伸爪，鼠丧残生。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"坚守本份相安无事。",
			"  您抽到的是第三二六签"+"\t"+""+"\t"+""+"\t"+"书中有女颜如玉，书中自有黄金屋，读尽五车书。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者应切记斯言，不再忧心忡忡书。",
			"  您抽到的是第三二七签"+"\t"+""+"\t"+""+"\t"+"豹变成文采，乘龙福自臻，赤身成富贵，事事得振新。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者将获佳婿，荣华富贵享用不尽。",
			"  您抽到的是第三二八签"+"\t"+""+"\t"+""+"\t"+"孤阳微兮，群阴溢兮，力既殚兮。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签寓有「道消魔长」之意，处此情况唯有谨慎或可保全。",
			"  您抽到的是第三二九签"+"\t"+""+"\t"+""+"\t"+"晓雨初晴映碧溪，重重春色上柴扉。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"家道富裕，宜安於现状，不必另起为官或经商之念。",
			"  您抽到的是第三三○签"+"\t"+""+"\t"+""+"\t"+"世道多荆棘，人情每用嗟，利名如有路，勤苦逐生涯。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"名利与己与缘，安份守己勤苦渡日最宜。",
			"  您抽到的是第三三一签"+"\t"+""+"\t"+""+"\t"+"山穷路转迷，水急舟难渡，万事莫强为，出处遭研妒。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"跋山涉水出门远行，均属不利。",
			"  您抽到的是第三三四签"+"\t"+""+"\t"+""+"\t"+"自从持守定，功在众人先，别有非常喜，随龙到九天。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一人做任何事，具有毅力，则其成功机会较大。",
			"  您抽到的是第三三五签"+"\t"+""+"\t"+""+"\t"+"远涉波涛一叶舟，而今始得过滩头。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签甚佳，凡事亨通。",
			"  您抽到的是第三三六签"+"\t"+""+"\t"+""+"\t"+"受君之禄，盈而不覆，守之乃昌，永保安康。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得签者久受重用，生活富裕。",
			"  您抽到的是第三三七签"+"\t"+""+"\t"+""+"\t"+"万事不由人计较，一生尽是命安排，莫疑猜！"+"\t"+""+"\t"+""+"\t"+""+"\t"+"谋事在人，成事在天。",
			"  您抽到的是第三三九签"+"\t"+""+"\t"+""+"\t"+"两女一夫，上下相祛，阴气乘阳，用是耗虚。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"对女色之困惑，须加以警惕，否则将有耗虚之虞。",
			"  您抽到的是第三四○签"+"\t"+""+"\t"+""+"\t"+"双燕归南国，来寻王谢家，华堂春尽静，进此托生涯。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"倦游归来，景物全非。",
			"  您抽到的是第三四一签"+"\t"+""+"\t"+""+"\t"+"命运蹇兮时违，灾殃及兮身疲。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不吉，诸事不宜。",
			"  您抽到的是第三四二签"+"\t"+""+"\t"+""+"\t"+"采药天台路转迷，桃花流水赋佳期。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"在他乡工作时意外得遇知音，事事顺遂，暂且不宜返乡。",
			"  您抽到的是第三四三签"+"\t"+""+"\t"+""+"\t"+"出温入寒，被薄衣单，去我慈航，难解横愆。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"因时运不济及自己过错，应借助信仰力量以求自救。",
			"  您抽到的是第三四四签"+"\t"+""+"\t"+""+"\t"+"三升三石放在一斗，满而溢，子自得。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"示人不可削足适屦，亦寓有「满招损」之意。",
			"  您抽到的是第三四六签"+"\t"+""+"\t"+""+"\t"+"云散月当空，牛前马後逢，张弓方抵御，一箭定全功。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"鼠年及羊年会有一举成功机会。",
			"  您抽到的是第三四八签"+"\t"+""+"\t"+""+"\t"+"云横山际水茫茫，千里长途望故乡。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"困厄之事临身，一时难以解除，惆怅难免，怨恨则不必。",
			"  您抽到的是第三四九签"+"\t"+""+"\t"+""+"\t"+"风波今已息，舟楫遇安流，自此功名遂，何须叹白头。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一切纷争已息，今後行程应平稳无险？",
			"  您抽到的是第三五○签"+"\t"+""+"\t"+""+"\t"+"巴到平安地，江山万里程，绿杨芳草处，风快马蹄轻。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"历尽千辛万苦，如今终入佳境。",
			"  您抽到的是第三五一签"+"\t"+""+"\t"+""+"\t"+"雕鹗当秋势转雄，乘风分翼到蟾宫。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"时来运转，气势甚雄，做任何事均能顺利无比。",
			"  您抽到的是第三五二签"+"\t"+""+"\t"+""+"\t"+"鼠为患终宵不得宁，猫儿一叫几夜太平。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"小人得志，嚣张狂妄，但为时不长。",
			"  您抽到的是第三五三签"+"\t"+""+"\t"+""+"\t"+"两人在旁，太阳在上，照汝一寸心，仙机曾否明。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"举头三尺有神明，与人相处莫欺心。",
			"  您抽到的是第三五四签"+"\t"+""+"\t"+""+"\t"+"阴气郁郁，阳气不扬，如何如何，良贾深藏。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"若大丈夫怀才不遇，则深藏不露乃为上策。",
			"  您抽到的是第三五五签"+"\t"+""+"\t"+""+"\t"+"去到长安，东北转角，逢着天门，便有下落。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"未明，视所问事而定。",
			"  您抽到的是第三五六签"+"\t"+""+"\t"+""+"\t"+"徐步入天台，为听好消息，采药有仙童，洞府列春色。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"机运不能坐等，要亲自向可能之处探寻。",
			"  您抽到的是第三五七签"+"\t"+""+"\t"+""+"\t"+"思量一夜，不如打干一番，若还错，烦恼及肺肝。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签寓意至明。",
			"  您抽到的是第三五八签"+"\t"+""+"\t"+""+"\t"+"心细胆粗，可胜上将之任，勇往前行成败何必在心。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签寓意至明。",
			"  您抽到的是第三五九签"+"\t"+""+"\t"+""+"\t"+"天念苦修人，终不落红尘，清心能见道，扰扰丧真灵。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"对某件事能专心致志去做，收获必不同凡响。",
			"  您抽到的是第三六一签"+"\t"+""+"\t"+""+"\t"+"手持一木鱼，沿街去化缘，不见徐公来，却遇一鸟去。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事恐徒劳无功。",
			"  您抽到的是第三六二签"+"\t"+""+"\t"+""+"\t"+"水火既济，阴阳相契，育物新民，参天赞地。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所有冲突已化解，适宜做任何事。",
			"  您抽到的是第三六四签"+"\t"+""+"\t"+""+"\t"+"一个神道，随尔去行，逢人说法，到处显灵。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者手法高明，待人处事八面玲珑。",
			"  您抽到的是第三六五签"+"\t"+""+"\t"+""+"\t"+"炉中火，沙里金，功力到，丹鼎成。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"功夫到了，任何事均可以做成。",
			"  您抽到的是第三六六签"+"\t"+""+"\t"+""+"\t"+"此去万里程，却遇见知音，同心共济，大立勋名。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"在当地遇知音，成家立业，赢得声名。",
			"  您抽到的是第三六七签"+"\t"+""+"\t"+""+"\t"+"寻芳春日，适见花开，朵朵堪摘，枝枝可栽。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"做任何事要能适当其时，才会处处逢源，事事顺心。",
			"  您抽到的是第三六八签"+"\t"+""+"\t"+""+"\t"+"龙一吟，云便兴，冲霄直上，快睹太平。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"所问之事，已出现祥瑞之兆，很快就有和谐局面出现。",
			"  您抽到的是第三六九签"+"\t"+""+"\t"+""+"\t"+"虎出金榜，有勇亦何济，怎似山翁，非富犹有趣。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"不能以常理处之，此时须求助於专门人才。",
			"  您抽到的是第三七○签"+"\t"+""+"\t"+""+"\t"+"过羊肠，入康庄，五陵裘马，当思故乡。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"历尽辛苦，须饮水思源不可忘本。",
			"  您抽到的是第三七一签"+"\t"+""+"\t"+""+"\t"+"火遭水克，火灭其光，水势滔滔，源远流长。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"处理紧急情况，不能急切，须防後患。",
			"  您抽到的是第三七二签"+"\t"+""+"\t"+""+"\t"+"东阁筵开，佳客自来，高歌唱和，展挹舒怀。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得签人已颇有名望，往来多为高雅之士。",
			"  您抽到的是第三七三签"+"\t"+""+"\t"+""+"\t"+"世界似清宁，不知辞已休，打叠要小心，须防遭火 。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"天下本无事，庸人自扰之。",
			"  您抽到的是第三七四签"+"\t"+""+"\t"+""+"\t"+"跳龙门，须激浪，雷电轰轰，踊跃万丈。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"要达崇高目标，须有超人本事，并能借机乘势。",
			"  您抽到的是第三七五签"+"\t"+""+"\t"+""+"\t"+"山上有古松，亭亭冲汉斗，干老枝更长，天地生荣久。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"顺其自然，把握有利机运去求发展。",
			"  您抽到的是第三七七签"+"\t"+""+"\t"+""+"\t"+"一头猪，可祭天地，虽丧身，亦算好处。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"天生万物必有用，寓意人万勿连猪狗都不如。",
			"  您抽到的是第三七八签"+"\t"+""+"\t"+""+"\t"+"与其日营营，何如夜忖忖，日里多劳形，夜间却安稳。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"此签寓意至明。",
			"  您抽到的是第三七九签"+"\t"+""+"\t"+""+"\t"+"东风来，花自开，大家喝采，畅饮三杯。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"万事俱备，只欠东风，如今东风来了，自是令人开怀之事。",
			"  您抽到的是第三八○签"+"\t"+""+"\t"+""+"\t"+"疏食饮水，乐在其中，膏梁美味，反使心朦。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"知足常乐。",
			"  您抽到的是第三八一签"+"\t"+""+"\t"+""+"\t"+"黄牛辟土，大力开疆，西成时候，谷米盈仓。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"一分耕耘，一分收获。",
			"  您抽到的是第三八二签"+"\t"+""+"\t"+""+"\t"+"蛇可化龙，头角将出，平地一声雷，方显龙蛇力。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得此签者在平凡中力求不凡，即将有所成就。",
			"  您抽到的是第三八三签"+"\t"+""+"\t"+""+"\t"+"九华山顶，紫气腾腾，异尽一舟，取去前行。"+"\t"+""+"\t"+""+"\t"+""+"\t"+"得签者可望取得通达之机缘，从此一帆风顺。",
			};
	
	static ZbFont zbfont = ZbFont.隶书;
	static int fontSize = 28;
	static Color color = new Color(0, 0, 0);
	static int fontType = Font.BOLD;
	
	@Override
	public String draw(String one, String tmpBackPic, String count) throws IOException {
		
		int n = one.length();
		
		//姓名
		SimplePositions nameSP = new SimplePositions();
		nameSP.buildHorizontalOffset(260-(n*44/2)).buildVerticalOffset(230);
		BufferedImage nameBI = drawText(one, zbfont, 44, color,
				250, 150, 0, true);
		//神签
		
		int max = shenQian.length;
		Random ran = new Random();
		int num = ran.nextInt(max);
		SimplePositions nameSP2 = new SimplePositions();
		nameSP2.buildHorizontalOffset(90).buildVerticalOffset(290);
		BufferedImage nameBI2 = drawText(shenQian[num], zbfont, fontSize, color,
				340, 500, 0, true);
		
		//未来身价
		DecimalFormat df=new DecimalFormat("00000000");
		SimplePositions count0 = new SimplePositions();
		count0.buildHorizontalOffset(222).buildVerticalOffset(816);
		BufferedImage countBI0 = drawText(df.format(Integer.parseInt(count)), ZbFont.黑体, 33, color,
				300, 0, 0, true);
		
		
		
		SimplePositions[] sp = {nameSP,nameSP2,count0};

		BufferedImage[] bis = { nameBI,nameBI2,countBI0};

		return super.getSaveFile(sp, bis, 0.8f, tmpBackPic);
	}

}
