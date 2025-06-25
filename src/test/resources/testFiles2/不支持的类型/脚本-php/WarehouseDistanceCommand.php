<?php

namespace App\Command;

use App\Service\Distance\AddressService;
use Hyperf\Command\Command as HyperfCommand;
use Psr\Container\ContainerInterface;
use Hyperf\Command\Annotation\Command;
use App\Dao\StoreDao;

/**
 * @Command
 */
class WarehouseDistanceCommand extends HyperfCommand
{
    /**
     * @var ContainerInterface
     */
    protected $container;
    protected $name = 'timer:warehouse-distance';

    public function __construct(ContainerInterface $container)
    {
        $this->container = $container;

        parent::__construct();
    }

    public function configure()
    {
        parent::configure();
        $this->setDescription('计算所有仓库两两之间的距离');
    }

    public function handle()
    {
        //仓库之间的距离缓存预热
        $addressService = $this->container->get(AddressService::class);
        if (!empty($addressService)) {
            $addressService->warehouse2warehouseDistance2Cache();
        }

        $storeDao = $this->container->get(StoreDao::class);
        if (!empty($storeDao)) {
            $storeDao->getSpecialStoreList();
        }

    }
}