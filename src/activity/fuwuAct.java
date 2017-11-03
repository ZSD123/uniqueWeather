package activity;


import com.sharefriend.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class fuwuAct extends baseActivity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fuwu);
		TextView textView=(TextView)findViewById(R.id.fuwu);
		textView.setText("欢迎您使用共享交友软件及服务！"+"\n"+"     为使用本软件及服务，您应当阅读并遵守该协议。"+
		           "请您务必仔细阅读、充分理解各条款内容。除非您已阅读并接受本协议所有条款，否则您无权下载、安装"+
				"或使用本软件及相关服务。您的下载、安装、使用、登录等行为即视为您已阅读并同意本协议的约束。"+"\n"+"\n"+
		           "一、协议的范围"+"\n"+"     本协议是您下载、安装、使用、登录本软件，以及使用本服务所订立的协议。"+"\n"+"\n"+
				"二、关于本服务"+"\n"+"2.1本服务内容"+"\n"+"     本服务内容是指开发者通过本软件向用户提供相关的服务。"+"\n"+
		           "2.2本服务形式"+"\n"+"     您可以通过手机使用本服务，目前只有安卓手机上有，十分抱歉，不过，后期会加IOS等一系列客户端为您提供更好的服务，"+
				"同时，会不断丰富您使用本服务的终端、形式以及功能。"+"\n"+"2.3 许可范围"+"\n"+"     2.3.1 开发者给予您一项个人的、不可转让及非排他性的许可，"+
		        "以使用本软件。您可以为非商业目的在单一台终端设备上下载、安装、使用、登录本软件。"+"\n"+"     2.3.2您可以制作本软件的一个副本，"+
				"仅用作备份。备份副本必须包含原软件中含有的所有著作权信息。"+"\n"+"     2.3.3本条及本协议其他条款未明示授权的其他一切权利仍由开发者保留，"+
		        "您在行驶这些权利时须另外取得开发者的书面许可。开发者如果未行使前面的任何权利，并不构成对该权利的放弃。"+"\n"+"\n"+"三、软件的获取"+"\n"+
				"     3.1您可以直接从开发者授权的各大安卓应用市场获取"+"\n"+"     3.2如果您从未经开发者授权的第三方获取本软件或与本软件名称相同的安装程序，"+
		        "开发者无法保证该软件能够正常使用，并对因此给您造成的损失不予负责。"+"\n"+"\n"+"四、软件的安装与卸载"+"\n"+"     4.1下载安装程序后，您需要按照该程序提示的步骤正确安装。"+"\n"+
				"     4.2如果您不再需要使用本软件或安装新版软件，可以自行卸载。如果您愿意帮助开发者改进产品服务，请告知卸载的原因。"+"\n"+"\n"+
		        "五、软件的更新"+"\n"+"     5.1为了增进用户体验、完善服务内容，开发者将不断努力开发新的服务，并为您不时提供软件更新（这些更新可能包括替换、修改、功能强化等）。"+"\n"+
				"     5.2为了改善用户体验，并保证服务的安全性和功能的一致性，开发者有权不经向您特别通知而对软件进行更新，或者对软件的部分功能效果进行改变或限制。"+"\n"+
		        "     5.3本软件新版本发布后，旧版本的软件可能无法使用。开发者不保证旧版本软件继续可用及相应的客户服务，请您随时核对并下载最新版本。"+"\n"+"\n"+
				"六、用户个人信息保护"+"\n"+"     6.1保护用户个人信息是开发者所必要做的，开发者会采取合理的措施保护用户的个人信息。除法律法规规定的情形外，未经用户许可开发者不会向第三方公开、"+
		        "透露用户个人信息。开发者对相关信息采用有效的加密存储与传输方式，保障用户个人信息的安全。"+"\n"+"     6.2您在注册帐号或使用本服务的过程中，可能需要填写一些必要的信息。若国家法律法规或政策有特殊规定的，您需要填写真实的身份信息。若您填写的信息不完整，则无法使用本服务或在使用过程中受到限制。"+"\n"+
				"     6.3 一般情况下，您可随时浏览、修改自己提交的信息，但出于安全性和身份识别的考虑，您可能无法修改注册时提供的初始注册信息及其他验证信息。"+"\n"+
		        "     6.4 开发者将运用各种安全技术和程序建立完善的管理制度来保护您的个人信息，以免遭受未经授权的访问、使用或披露。"+"\n"+
				"     6.5若您是18周岁以下的未成年人，在使用本服务前，应事先取得您家长或法定监护人的书面同意。"+"\n"+"\n"+
		        "七、用户行为规范"+"\n"+"7.1用户注意事项"+"\n"+"     7.1.1请您充分理解并同意：为了向您提供更好的网络社交服务，本服务的某些功能可能会让第三方知晓您的某些信息，包括但不限于您的好友可以查询您提交的可公开的个人资料，以及本软件的地理位置服务，可能会获取并展示您的位置、状态等。您应了解本部分的信息公开和透露可能会给您带来潜在风险，并自行承担由此造成的风险。"+"\n"+
				"     7.1.2您理解并同意：开发者将会尽其商业上的合理努力保障您在本软件及服务中的数据存储安全，但是，开发者并不能就此提供完全保证，包括但不限于以下情形："+"\n"+
		        "     （1）开发者不对您在本软件及服务中相关数据的删除或储存失败负责；"+"\n"+"     （2）开发者有权根据实际情况自行决定您在本软件及服务中数据的最长储存期限、服务器中数据的最大存储空间等，您可根据自己的需要自行备份本软件及服务中的相关数据。双方另有约定的按相应约定执行；"+"\n"+
				"     （3）如果您停止使用本软件及服务或服务被终止或取消，开发者可以从服务器上永久地删除您的数据。您的服务停止、终止或取消后，开发者没有义务向您返还任何数据。"+"\n"+
		        "     （4）由于本软件是基于第三方平台开发，由于第三方平台的原因而导致数据丢失等一系列情况，开发者不对此负责。"+"\n"+
				"     7.1.3本服务会向您提供地理位置的交友功能，您使用这些功能即表示您同意本服务获取、使用您的地理位置、兴趣爱好等信息。同时，您理解并同意：在前述相关功能中，相关信息的发布由用户自行发布，并自行确保其合法、健康、真实以及自行承担相关法律责任； 您应自行通过各种手段认真核实相关用户及其发布信息的合法性、真实性，若您与其他用户涉及线下见面、约会或交友等各种实体活动的，在进行线下活动时，要注意保护好自己的人生、财产安全；"+"\n"+
		        "     同时，开发者在前述相关功能中，仅提供技术支持和平台服务，基于互联网虚拟的特性，开发者无法核实相关用户及发布信息的真实性、合法性，也无法向您提供任何保证，若由于前述相关功能，导致您在线上、线下有任何人身、财产损失的，您同意自行承担全部责任，与开发者无关也不会追究开发者的任何责任。"+"\n"+"7.2.1用户禁止事项"+"\n"+"     您在使用本服务时不得利用本服务从事以下行为，包括但不限于："+"\n"+
				"     （1）发布、传送、传播、储存违反国家法律、危害国家安全统一、社会稳定、公序良俗、社会公德以及侮辱、诽谤、淫秽、暴力的内容；"+"\n"+"     （2）发布、传送、传播、储存侵害他人名誉权、肖像权、知识产权、商业秘密等合法权利的内容；"+"\n"+"     （3）虚构事实、隐瞒真相以误导、欺骗他人；"+"\n"+"     （4）发表、传送、传播广告信息及垃圾信息；"+"\n"+"     （5）从事其他违反法律法规、政策及公序良俗、社会公德等的行为。"+"\n"+
		        "7.2.2除非法律允许或开发者书面许可，您不得从事下列行为："+"\n"+"     （1）删除本软件及其副本上关于著作权的信息；"+"\n"+"     （2）对本软件进行反向工程、反向汇编、反向编译，或者以其他方式尝试发现本软件的源代码；"+"\n"+
				"     （3）对本软件或者本软件运行过程中释放到任何终端内存中的数据、软件运行过程中客户端与服务器端的交互数据，以及本软件运行所必需的系统数据，进行复制、修改、增加、删除、挂接运行或创作任何衍生作品，形式包括但不限于使用插件、外挂或非开发者经授权的第三方工具/服务接入本软件和相关系统；"+"\n"+
		        "     （4）通过修改或伪造软件运行中的指令、数据，增加、删减、变动软件的功能或运行效果，或者将用于上述用途的软件、方法进行运营或向公众传播，无论这些行为是否为商业目的；"+"\n"+"     （5）通过非本开发者开发、授权的第三方软件、插件、外挂、系统，登录或使用本开发者软件及服务，或制作、发布、传播上述工具；"+"\n"+
				"     （6）自行、授权他人或利用第三方软件对本软件及其组件、模块、数据等进行干扰；"+"\n"+"     （7）进行任何超出正常的好友或用户之间沟通、交流等目的的行为；"+"\n"+"     （8）出于超出正常好友或用户之间沟通、交流等目的（包括但不限于为发送广告、垃圾、骚扰或违法违规等信息的目的），通过自己添加或诱导他人添加等任何方式使自己与其他用户形成好友关系（好友关系包括但不限于单向好友关系和双向好友关系，下同）；"+"\n"+
		        "     （9）若您违反前述约定、本协议或相关法规政策，开发者有权不经您同意而直接解除您的好友关系；若您的单向或双向好友(简称“违规用户”)违反前述约定、本协议或相关法规政策，开发者有权按照本协议、相关协议或相关法规政策等，对违规用户进行处理，由此可能影响您与违规用户之间的信息交流、好友关系等以及您对本服务的使用，同时您知悉：这是开发者为了维护健康良好的网络环境而采取的必要措施，若由于开发者按照前述约定对违规用户采取措施而对您产生影响或任何损失的，您同意不追究开发者的任何责任或不向开发者主张任何权利。"+"\n"+
				"     （10）通过本服务向好友或其他用户发送大量信息；"+"\n"+"     （11）其他未经开发者明示授权、许可或违反本协议及相关协议、规则的行为。"+"\n"+"7.3对自己的行为负责"+"\n"+"     您充分了解并同意，您必须为自己注册帐号下的一切行为负责，包括您所发表的任何内容以及由此产生的任何后果。您应对使用本服务时接触到的内容自行加以判断，并承担因使用内容而引起的所有风险，包括因对内容的正确性、完整性或实用性的依赖而产生的风险。开发者无法且不会对您因前述风险而导致的任何损失或损害承担责任。"+"\n"+
		        "7.4违约处理"+"\n"+"     如果开发者发现或收到他人举报您有违反本协议约定的，开发者有权不经通知随时对相关内容进行删除、屏蔽，并采取包括但不限于暂停、终止您使用相应账户，暂停、终止您使用部分或全部本服务，追究法律责任等措施。"+"\n"+"7.5      对第三方损害的处理"+"\n"+"您违反约定，导致任何第三方损害的，您应当独立承担责任；开发者因此遭受损失的，您也应当一并赔偿。"+"\n"+"\n"+"八、对第三方损害的处理"+"\n"+
				"     您在本软件上使用第三方提供的产品或服务时，除遵守本协议约定外，还应遵守第三方的用户协议。开发者和第三方对可能出现的纠纷在法律规定和约定的范围内各自承担责任。"+"\n"+"因用户使用本软件或要求开发者提供特定服务时，本软件可能会调用第三方系统或者通过第三方支持用户的使用或访问，使用或访问的结果由该第三方提供（包括但不限于您通过本服务跳转到的第三方提供的服务及内容，第三方通过本软件接入的服务及内容等），开发者无法保证第三方提供服务及内容的安全性、准确性、有效性及其他不确定的风险，您应自行承担相关责任。"+"\n"+"\n"+
		        "九知识产权声明"+"\n"+"     9.1本开发者是本软件的知识产权权利人。本软件的著作权等知识产权，以及与本软件相关的所有信息内容（包括但不限于文字、图片、音频、视频、图表、界面设计、版面框架、有关数据或电子文档等）均受中华人民共和国法律法规和相应的国际条约保护，本开发者依法享有上述相关知识产权。"+"\n"+"     9.2 未经本开发者书面同意，您不得为任何商业或非商业目的自行或许可任何第三方实施、利用、转让上述知识产权。"+"\n"+"     9.3凡是发现app内有侵犯您合法权利的内容，请立即通过客服或者邮箱联系我们，我们第一时间处理，积极维护您的合法权益。"+"\n"+
				"十终端安全责任"+"\n"+"     10.1您理解并同意，本软件或本服务同大多数互联网软件、服务一样，可能会受多种因素影响（包括但不限于用户原因、网络服务质量、社会环境等）；也可能会受各种安全问题的侵扰（包括但不限于他人非法利用用户资料，进行现实中的骚扰；用户下载安装的其他软件或访问的其他网站中可能含有病毒、木马程序或其他恶意程序，威胁您终端的信息和数据的安全，继而影响本软件、本服务的正常使用等）。因此，您应加强信息安全及个人信息的保护意识，注意密码保护，以免遭受损失。"+"\n"+
		        "     10.2您不得制作、发布、使用、传播用于窃取本软件账户及他人个人信息、财产的恶意程序。"+"\n"+"     10.3维护软件安全与正常使用是本开发者和您的共同责任，本开发者将按照行业标准合理审慎地采取必要技术措施保护您的终端信息和数据安全，但是您承认和同意开发者不能就此提供任何保证。"+"\n"+
				"     10.4在任何情况下，您不应轻信借款、索要密码或其他涉及财产的网络信息。涉及财产操作的，请一定先核实对方身份。"+"\n"+"\n"+"十一第三方平台或技术"+"\n"+"     11.1本软件可能会使用第三方平台或技术（包括本软件可能使用的开源代码和公共领域代码等，下同），这种使用已经获得合法授权。"+"\n"+
		        "     11.2根据第三方政策,您不得在本平台上发布和散播任何形式的含有下列内容的信息："+"\n"+"          (1)违反国家法律法规政策的任何内容/信息；"+"\n"+"          (2)违反国家规定的政治宣传和/或新闻信息；"+"\n"+"          (3)涉及国家秘密和/或安全的信息；"+"\n"+"          (4)封建迷信和/或淫秽、色情、下流的信息或教唆犯罪的信息；"+"\n"+"          (5)违反国家民族和宗教政策的信息；"+"\n"+"          (6)防碍互联网运行安全的信息；"+"\n"+
		        "          (7)侵害他人合法权益的信息和/或其他有损于社会秩序、社会治安、公共道德的信息或内容；"+"\n"+"          (8)其他违反法律法规、部门规章或国家政策的内容；"+"\n"+"          (9)其他平台认为不适当在本平台发布的内容。"+"\n"+"     11.3如果发现本软件存在有侵犯您合法知识产权权利的一系列形式，对此表示抱歉，欢迎及时告知，本软件立即清理修正。"+"\n"+"\n"+
		        "十二其他"+"\n"+"     12.1 您使用本软件或本服务即视为您已阅读并同意受本协议的约束。开发者有权在必要时修改本协议条款。您可以在本软件、本服务的最新版本中查阅相关协议条款。本协议条款变更后，如果您继续使用本软件、本服务，即视为您已接受修改后的协议。如果您不接受修改后的协议，应当停止使用本软件。"+"\n"+"     12.2 本协议的成立、生效、履行、解释及纠纷解决，适用中华人民共和国大陆地区法律（不包括冲突法）。"+"\n"+"     12.3若您和本开发者之间发生任何纠纷或争议，应尽量友好协商解决。"+"\n"+
		        "     12.4 本协议所有条款的标题仅为阅读方便，本身并无实际涵义，不能作为本协议涵义解释的依据。(完)"+"\n"+"\n"+"                                 开发者:张圣德大帝"+"\n"+"                                 联系QQ或微信:1833751104"
				);
	}
      
} 
