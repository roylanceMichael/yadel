using System.Threading.Tasks;
namespace Org.Roylance.Yadel.Api
{
	public interface IHttpExecute
	{
		Task<string> PostAsync(string url, string base64Message);
	}
}